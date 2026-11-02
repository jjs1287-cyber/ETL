BEGIN;
CREATE TABLE IF NOT EXISTS audit.capture_configuration (
 singleton boolean PRIMARY KEY DEFAULT true CHECK(singleton),
 enabled boolean NOT NULL DEFAULT false,
 scope text NOT NULL DEFAULT 'Operations, Scripts and XSLT transforms'
);
REVOKE ALL ON audit.capture_configuration FROM PUBLIC;
INSERT INTO audit.capture_configuration(singleton) VALUES(true) ON CONFLICT DO NOTHING;
CREATE OR REPLACE VIEW audit.transformation_identity AS
 SELECT item_id,guid::text FROM public.guidsfortransformationstab
 UNION ALL
 SELECT v.transformation_id,'webservice:'||g.guid
 FROM public.webservicefunctiontransformationview v
 JOIN public.guidsforwebservicestab g ON g.item_id=v.webservice_id
 WHERE NOT EXISTS(SELECT 1 FROM public.guidsfortransformationstab t WHERE t.item_id=v.transformation_id);
REVOKE ALL ON audit.transformation_identity FROM PUBLIC;
COMMIT;
