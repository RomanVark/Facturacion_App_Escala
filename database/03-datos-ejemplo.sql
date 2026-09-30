-- Opcional: ejecutar una sola vez en una base vacía.
BEGIN;
INSERT INTO categoria(nombre,activa) VALUES ('Papelería',TRUE),('Tecnología',TRUE),('Hogar',TRUE);
INSERT INTO producto(codigo,nombre,categoria_id,precio_venta,existencia,ruta_imagen,activo)
VALUES ('P001','Cuaderno universitario',(SELECT min(id) FROM categoria WHERE nombre='Papelería'),85.50,24,'',TRUE),
       ('P002','Mouse inalámbrico',(SELECT min(id) FROM categoria WHERE nombre='Tecnología'),350.00,8,'',TRUE);
INSERT INTO cliente(nombre,documento,telefono,correo,direccion,activo)
VALUES ('Cliente de ejemplo','DEMO-001','8888-0000','cliente@example.com','Managua',TRUE);
COMMIT;
