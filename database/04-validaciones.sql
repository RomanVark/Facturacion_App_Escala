-- Para bases creadas con la versión anterior. Ejecutar conectado a tienda_javafx.
-- Si se informa un conflicto, corríjalo desde la aplicación/pgAdmin y reintente.
-- No elimina ni modifica registros. Todo se confirma en una sola transacción.
BEGIN;
LOCK TABLE categoria, producto IN SHARE ROW EXCLUSIVE MODE;
DO $$
BEGIN
 IF EXISTS (SELECT 1 FROM categoria GROUP BY LOWER(BTRIM(nombre)) HAVING COUNT(*)>1) THEN
  RAISE EXCEPTION 'Hay categorías con nombres duplicados (ignorando mayúsculas y espacios). Renómbrelas antes de continuar.';
 END IF;
 IF EXISTS (SELECT 1 FROM producto WHERE precio_venta<=0) THEN
  RAISE EXCEPTION 'Hay productos con precio cero o negativo. Corrija sus precios antes de continuar.';
 END IF;
END $$;
CREATE UNIQUE INDEX IF NOT EXISTS uq_categoria_nombre_normalizado ON categoria (LOWER(BTRIM(nombre)));
ALTER TABLE producto DROP CONSTRAINT IF EXISTS producto_precio_venta_check;
ALTER TABLE producto ADD CONSTRAINT producto_precio_venta_check CHECK (precio_venta>0);
COMMIT;
