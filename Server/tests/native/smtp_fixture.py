"""Loopback-only SMTP transport test. No external messages are sent."""
import email.parser
import email.policy
import socketserver
import subprocess
import sys
import threading

class Fixture(socketserver.StreamRequestHandler):
    def handle(self):
        self.connection.settimeout(20)
        self.wfile.write(b"220 localhost ETLForge fixture\r\n")
        envelope=[]
        while True:
            line=self.rfile.readline()
            if not line:
                return
            command=line.decode("ascii",errors="replace").strip()
            if command.upper().startswith(("EHLO", "HELO")):
                self.wfile.write(b"250-localhost\r\n250 SIZE 1048576\r\n")
            elif command.upper().startswith(("MAIL FROM:", "RCPT TO:")):
                envelope.append(command)
                self.wfile.write(b"250 OK\r\n")
            elif command.upper()=="DATA":
                self.wfile.write(b"354 End with dot\r\n")
                lines=[]
                while True:
                    part=self.rfile.readline()
                    if part==b".\r\n":
                        break
                    if not part:
                        raise AssertionError("Incomplete DATA")
                    lines.append(part[1:] if part.startswith(b"..") else part)
                self.server.messages.append((envelope,b"".join(lines)))
                self.wfile.write(b"250 Queued\r\n")
            elif command.upper()=="QUIT":
                self.wfile.write(b"221 Bye\r\n")
                return
            else:
                self.wfile.write(b"502 Unsupported\r\n")

with socketserver.TCPServer(("127.0.0.1",0),Fixture) as server:
    server.messages=[]
    thread=threading.Thread(target=server.serve_forever,daemon=True)
    thread.start()
    try:
        completed=subprocess.run([sys.argv[1],str(server.server_address[1])],capture_output=True,text=True,timeout=40)
        print(completed.stdout,end="")
        if completed.returncode:
            raise AssertionError(completed.stderr)
        assert len(server.messages)==1, "Unexpected extra delivery or missing message"
        envelope,raw=server.messages[0]
        addresses=[command[:command.index(">")+1] for command in envelope]
        assert addresses==["MAIL FROM:<bounce@example.invalid>","RCPT TO:<to@example.invalid>","RCPT TO:<cc@example.invalid>","RCPT TO:<hidden@example.invalid>"], envelope
        message=email.parser.BytesParser(policy=email.policy.default).parsebytes(raw)
        assert str(message["Subject"])=="ETLForge delivery \u00e9 \u65e5\u672c"
        assert message["From"].addresses[0].display_name=="Sender, Name"
        assert message["Reply-To"].addresses[0].addr_spec=="reply@example.invalid"
        assert message["Bcc"] is None, "Bcc must remain envelope-only"
        assert message.get_content()=="Line one\r\n.UTF-8 \u65e5\u672c\r\n"
        assert not message.defects
        print("PASS: actual SMTP envelope, To/Cc/Bcc, Reply-To, MIME/UTF-8 and one delivery; loopback only")
    finally:
        server.shutdown()
        thread.join()
