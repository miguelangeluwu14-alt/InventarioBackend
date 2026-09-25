-- StockAndes - datos semilla completos (3 categorías, 12 productos, 6 áreas)
-- Ejecutar conectado como INVENTARIODB después del primer arranque de Spring Boot.

INSERT INTO categorias (nombre, descripcion, estado, fecha_creacion)
 VALUES ('Útiles de oficina', 'Papelería y útiles de escritorio', 1, CURRENT_TIMESTAMP);
INSERT INTO categorias (nombre, descripcion, estado, fecha_creacion)
 VALUES ('Limpieza', 'Artículos de limpieza e higiene', 1, CURRENT_TIMESTAMP);
INSERT INTO categorias (nombre, descripcion, estado, fecha_creacion)
 VALUES ('Tecnología', 'Suministros y periféricos', 1, CURRENT_TIMESTAMP);

INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'OFI-001', 'Papel bond A4 x 500 hojas', 18.50, 120, 20, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Útiles de oficina';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'OFI-002', 'Lapicero azul caja x 12', 9.00, 40, 10, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Útiles de oficina';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'OFI-003', 'Archivador de palanca', 7.50, 5, 10, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Útiles de oficina';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'OFI-004', 'Engrapadora metálica', 22.00, 0, 5, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Útiles de oficina';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'LIM-001', 'Detergente industrial 1 kg', 12.00, 60, 15, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Limpieza';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'LIM-002', 'Papel higiénico x 24', 45.00, 10, 8, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Limpieza';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'TEC-001', 'Tóner HP 105A', 180.00, 12, 4, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Tecnología';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'TEC-002', 'Mouse óptico USB', 25.00, 30, 10, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Tecnología';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'TEC-003', 'Teclado USB', 38.00, 3, 5, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Tecnología';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'LIM-003', 'Alcohol en gel 1 litro', 16.00, 0, 5, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Limpieza';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'TEC-004', 'Cable HDMI 2 metros', 20.00, 25, 5, 0, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Tecnología';
INSERT INTO productos (codigo, nombre, costo_unitario, stock, stock_minimo, estado, categoria_id, fecha_creacion)
 SELECT 'LIM-004', 'Escoba industrial', 30.00, 20, 5, 1, id, CURRENT_TIMESTAMP
 FROM categorias WHERE nombre = 'Limpieza';

INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR01', 'Secretaría Académica', 'Lucía Ramos', 'secretaria@stockandes.pe', 1500.00, 1, CURRENT_TIMESTAMP);
INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR02', 'Laboratorio de Cómputo', 'Pedro Salas', 'laboratorio@stockandes.pe', 2500.00, 1, CURRENT_TIMESTAMP);
INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR03', 'Biblioteca', 'Rosa Ticona', 'biblioteca@stockandes.pe', 800.00, 1, CURRENT_TIMESTAMP);
INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR04', 'Mantenimiento', 'Julio Pari', 'mantenimiento@stockandes.pe', 1000.00, 0, CURRENT_TIMESTAMP);
INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR05', 'Administración', 'Carmen Soto', 'administracion@stockandes.pe', 3000.00, 1, CURRENT_TIMESTAMP);
INSERT INTO areas (codigo, nombre, responsable, email, presupuesto_mensual, estado, fecha_creacion)
 VALUES ('AR06', 'Bienestar Estudiantil', 'Marco León', 'bienestar@stockandes.pe', 1200.00, 1, CURRENT_TIMESTAMP);

COMMIT;
