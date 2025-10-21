
-- Script para crear tablas principales
-- La base de datos bdd_apex_core_banco se crea automáticamente por Docker
-- PostgreSQL no usa USE, el script se ejecuta directamente en la base de datos

CREATE TABLE IF NOT EXISTS client (
    client_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    age INTEGER,
    identification VARCHAR(20) UNIQUE NOT NULL,
    address VARCHAR(200),
    phone VARCHAR(20),
    password VARCHAR(100),
    status BOOLEAN
);

CREATE TABLE account (
    account_id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(20),
    initial_balance DOUBLE PRECISION,
    status BOOLEAN,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);

CREATE TABLE transaction (
    transaction_id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP,
    transaction_type VARCHAR(20),
    amount DOUBLE PRECISION,
    balance DOUBLE PRECISION,
    account_id BIGINT,
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);



-- Insert sample data into client
INSERT INTO client (name, gender, age, identification, address, phone, password, status) VALUES
-- Clientes del ejercicio de ejemplo
('Jose Lema', 'M', 35, '1725485963', 'Otavalo sn y principal', '098254785', '1234', true),
('Marianela Montalvo', 'F', 28, '1745896523', 'Amazonas y NNUU', '097548965', '5678', true),
('Juan Osorio', 'M', 32, '1798745896', '13 junio y Equinoccial', '098874587', '1245', true),
-- Otros clientes de prueba
('Juan Perez', 'M', 30, '1234567890', 'Street 123', '0999999999', 'secret', true),
('Maria Gomez', 'F', 28, '0987654321', 'Avenue 456', '0988888888', 'key', true),
('Carlos Ruiz', 'M', 35, '1122334455', 'Park Ave', '0977777777', 'pass123', true),
('Ana Torres', 'F', 40, '2233445566', 'Main St', '0966666666', 'mypassword', true),
('Luis Martinez', 'M', 22, '3344556677', 'Central Ave', '0955555555', 'luispass', true),
('Sofia Lopez', 'F', 27, '4455667788', 'North St', '0944444444', 'sofiakey', true),
('Pedro Jimenez', 'M', 31, '5566778899', 'South Ave', '0933333333', 'pedro123', true),
('Lucia Castro', 'F', 29, '6677889900', 'West St', '0922222222', 'lucia456', true),
('Miguel Angel', 'M', 45, '7788990011', 'East Ave', '0911111111', 'miguelpass', true),
('Valeria Paredes', 'F', 33, '8899001122', 'Sunset Blvd', '0900000000', 'valeriapass', true),
('Jorge Salas', 'M', 38, '9900112233', 'Lake St', '0899999999', 'jorgepass', true),
('Camila Rios', 'F', 24, '1011121314', 'River Ave', '0888888888', 'camila123', true),
('Andres Vera', 'M', 36, '1112131415', 'Hill St', '0877777777', 'andrespass', true),
('Paula Mendoza', 'F', 41, '1213141516', 'Ocean Ave', '0866666666', 'paulapass', true),
('Diego Ortiz', 'M', 26, '1314151617', 'Forest St', '0855555555', 'diegopass', true);

-- Insert sample data into account
INSERT INTO account (account_number, account_type, initial_balance, status, client_id) VALUES
-- Cuentas del ejercicio de ejemplo (client_id 1=Jose Lema, 2=Marianela Montalvo, 3=Juan Osorio)
('478758', 'SAVINGS', 2000.00, true, 1),     -- Jose Lema - Ahorro
('225487', 'CHECKING', 100.00, true, 2),      -- Marianela Montalvo - Corriente
('495878', 'SAVINGS', 0.00, true, 3),         -- Juan Osorio - Ahorros
('496825', 'SAVINGS', 540.00, true, 2),       -- Marianela Montalvo - Ahorros
('585545', 'CHECKING', 1000.00, true, 1),     -- Jose Lema - Corriente (cuenta nueva)
-- Otras cuentas de prueba
('010001', 'SAVINGS', 1000.00, true, 4),
('010002', 'CHECKING', 500.00, true, 5),
('010003', 'SAVINGS', 1500.00, true, 6),
('010004', 'CHECKING', 2000.00, true, 7),
('010005', 'SAVINGS', 1200.00, true, 8),
('010006', 'CHECKING', 800.00, true, 9),
('010007', 'SAVINGS', 950.00, true, 10),
('010008', 'CHECKING', 1100.00, true, 11),
('010009', 'SAVINGS', 1750.00, true, 12),
('010010', 'CHECKING', 600.00, true, 13),
('010011', 'SAVINGS', 1300.00, true, 14),
('010012', 'CHECKING', 700.00, true, 15),
('010013', 'SAVINGS', 1600.00, true, 16),
('010014', 'CHECKING', 900.00, true, 17),
('010015', 'SAVINGS', 1400.00, true, 18);


-- Insert sample data into transaction
INSERT INTO transaction (date, transaction_type, amount, balance, account_id) VALUES
-- Movimientos del ejercicio de ejemplo
-- Cuenta 478758 (account_id 1) - Jose Lema - Ahorro: Retiro de 575
('2022-02-08 10:00:00', 'WITHDRAWAL', -575.00, 1425.00, 1),
-- Cuenta 225487 (account_id 2) - Marianela Montalvo - Corriente: Depósito de 600
('2022-02-10 14:30:00', 'DEPOSIT', 600.00, 700.00, 2),
-- Cuenta 495878 (account_id 3) - Juan Osorio - Ahorros: Depósito de 150
('2022-02-09 09:15:00', 'DEPOSIT', 150.00, 150.00, 3),
-- Cuenta 496825 (account_id 4) - Marianela Montalvo - Ahorros: Retiro de 540
('2022-02-08 11:45:00', 'WITHDRAWAL', -540.00, 0.00, 4),
-- Otros movimientos de prueba
-- Client 4 (account_id 6)
('2025-10-17 09:15:23', 'DEPOSIT', 500.00, 1500.00, 6),
('2025-10-17 14:22:45', 'WITHDRAWAL', -100.00, 1400.00, 6),
('2025-10-18 09:18:12', 'DEPOSIT', 200.00, 1600.00, 6),
('2025-10-18 11:35:56', 'WITHDRAWAL', -50.00, 1550.00, 6),
('2025-10-18 15:42:18', 'DEPOSIT', 300.00, 1850.00, 6),
('2025-10-19 09:28:33', 'WITHDRAWAL', -150.00, 1700.00, 6),
('2025-10-19 10:55:07', 'DEPOSIT', 400.00, 2100.00, 6),
('2025-10-19 11:11:42', 'WITHDRAWAL', -200.00, 1900.00, 6),
('2025-10-19 12:48:59', 'DEPOSIT', 250.00, 2150.00, 6),
('2025-10-19 13:33:14', 'WITHDRAWAL', -80.00, 2070.00, 6),
-- Client 5 (account_id 7)
('2025-10-16 11:25:48', 'DEPOSIT', 300.00, 800.00, 7),
('2025-10-16 15:37:22', 'WITHDRAWAL', -50.00, 750.00, 7),
('2025-10-17 11:44:55', 'DEPOSIT', 150.00, 900.00, 7),
('2025-10-17 14:12:38', 'WITHDRAWAL', -100.00, 800.00, 7),
('2025-10-18 11:58:14', 'DEPOSIT', 200.00, 1000.00, 7),
('2025-10-18 13:06:27', 'WITHDRAWAL', -80.00, 920.00, 7),
('2025-10-19 09:41:53', 'DEPOSIT', 250.00, 1170.00, 7),
('2025-10-19 10:29:09', 'WITHDRAWAL', -120.00, 1050.00, 7),
('2025-10-19 11:16:45', 'DEPOSIT', 300.00, 1350.00, 7),
('2025-10-19 12:52:31', 'WITHDRAWAL', -60.00, 1290.00, 7),
-- Client 6 (account_id 8)
('2025-10-15 12:08:17', 'DEPOSIT', 400.00, 1900.00, 8),
('2025-10-16 12:45:39', 'WITHDRAWAL', -150.00, 1750.00, 8),
('2025-10-17 12:31:52', 'DEPOSIT', 350.00, 2100.00, 8),
('2025-10-17 18:19:26', 'WITHDRAWAL', -100.00, 2000.00, 8),
('2025-10-18 12:54:08', 'DEPOSIT', 200.00, 2200.00, 8),
('2025-10-18 17:22:44', 'WITHDRAWAL', -80.00, 2120.00, 8),
('2025-10-19 09:37:15', 'DEPOSIT', 250.00, 2370.00, 8),
('2025-10-19 10:11:58', 'WITHDRAWAL', -120.00, 2250.00, 8),
('2025-10-19 11:49:33', 'DEPOSIT', 300.00, 2550.00, 8),
('2025-10-19 12:26:07', 'WITHDRAWAL', -60.00, 2490.00, 8),
-- Client 7 (account_id 9)
('2025-10-18 13:42:56', 'DEPOSIT', 600.00, 2600.00, 9),
('2025-10-18 17:18:23', 'WITHDRAWAL', -300.00, 2300.00, 9),
('2025-10-18 19:55:47', 'DEPOSIT', 500.00, 2800.00, 9),
('2025-10-19 08:33:11', 'WITHDRAWAL', -200.00, 2600.00, 9),
('2025-10-19 09:07:38', 'DEPOSIT', 400.00, 3000.00, 9),
('2025-10-19 10:44:52', 'WITHDRAWAL', -150.00, 2850.00, 9),
('2025-10-19 11:21:29', 'DEPOSIT', 350.00, 3200.00, 9),
('2025-10-19 11:58:14', 'WITHDRAWAL', -100.00, 3100.00, 9),
('2025-10-19 12:36:05', 'DEPOSIT', 250.00, 3350.00, 9),
('2025-10-19 13:12:49', 'WITHDRAWAL', -80.00, 3270.00, 9),
-- Client 8 (account_id 10)
('2025-10-17 10:28:41', 'DEPOSIT', 250.00, 1450.00, 10),
('2025-10-17 16:35:19', 'WITHDRAWAL', -100.00, 1350.00, 10),
('2025-10-18 09:48:52', 'DEPOSIT', 200.00, 1550.00, 10),
('2025-10-18 11:14:27', 'WITHDRAWAL', -50.00, 1500.00, 10),
('2025-10-18 16:51:33', 'DEPOSIT', 300.00, 1800.00, 10),
('2025-10-19 08:22:08', 'WITHDRAWAL', -150.00, 1650.00, 10),
('2025-10-19 09:39:44', 'DEPOSIT', 400.00, 2050.00, 10),
('2025-10-19 10:07:16', 'WITHDRAWAL', -200.00, 1850.00, 10),
('2025-10-19 11:54:29', 'DEPOSIT', 250.00, 2100.00, 10),
('2025-10-19 12:31:55', 'WITHDRAWAL', -80.00, 2020.00, 10),
-- Client 9 (account_id 11)
('2025-10-16 08:17:34', 'DEPOSIT', 350.00, 1150.00, 11),
('2025-10-16 18:42:58', 'WITHDRAWAL', -50.00, 1100.00, 11),
('2025-10-17 09:29:13', 'DEPOSIT', 200.00, 1300.00, 11),
('2025-10-17 18:56:47', 'WITHDRAWAL', -100.00, 1200.00, 11),
('2025-10-18 10:11:22', 'DEPOSIT', 150.00, 1350.00, 11),
('2025-10-18 18:38:59', 'WITHDRAWAL', -80.00, 1270.00, 11),
('2025-10-19 09:05:31', 'DEPOSIT', 250.00, 1520.00, 11),
('2025-10-19 10:52:14', 'WITHDRAWAL', -120.00, 1400.00, 11),
('2025-10-19 11:27:46', 'DEPOSIT', 300.00, 1700.00, 11),
('2025-10-19 12:44:09', 'WITHDRAWAL', -60.00, 1640.00, 11),
-- Client 10 (account_id 12)
('2025-10-17 07:53:28', 'DEPOSIT', 200.00, 1150.00, 12),
('2025-10-17 19:16:42', 'WITHDRAWAL', -80.00, 1070.00, 12),
('2025-10-18 08:49:57', 'DEPOSIT', 150.00, 1220.00, 12),
('2025-10-18 11:24:11', 'WITHDRAWAL', -100.00, 1120.00, 12),
('2025-10-18 19:37:26', 'DEPOSIT', 300.00, 1420.00, 12),
('2025-10-19 08:08:54', 'WITHDRAWAL', -150.00, 1270.00, 12),
('2025-10-19 09:51:38', 'DEPOSIT', 400.00, 1670.00, 12),
('2025-10-19 10:19:03', 'WITHDRAWAL', -200.00, 1470.00, 12),
('2025-10-19 11:42:17', 'DEPOSIT', 250.00, 1720.00, 12),
('2025-10-19 12:33:49', 'WITHDRAWAL', -80.00, 1640.00, 12),
-- Client 11 (account_id 13)
('2025-10-15 13:21:15', 'DEPOSIT', 500.00, 1600.00, 13),
('2025-10-15 19:38:52', 'WITHDRAWAL', -120.00, 1480.00, 13),
('2025-10-16 10:14:27', 'DEPOSIT', 200.00, 1680.00, 13),
('2025-10-16 17:47:09', 'WITHDRAWAL', -100.00, 1580.00, 13),
('2025-10-17 11:23:44', 'DEPOSIT', 150.00, 1730.00, 13),
('2025-10-17 18:56:18', 'WITHDRAWAL', -80.00, 1650.00, 13),
('2025-10-18 10:31:53', 'DEPOSIT', 250.00, 1900.00, 13),
('2025-10-18 19:08:29', 'WITHDRAWAL', -120.00, 1780.00, 13),
('2025-10-19 09:45:36', 'DEPOSIT', 300.00, 2080.00, 13),
('2025-10-19 11:19:04', 'WITHDRAWAL', -60.00, 2020.00, 13),
-- Client 12 (account_id 14)
('2025-10-18 08:44:51', 'DEPOSIT', 400.00, 2150.00, 14),
('2025-10-18 10:17:26', 'WITHDRAWAL', -150.00, 2000.00, 14),
('2025-10-18 11:53:08', 'DEPOSIT', 350.00, 2350.00, 14),
('2025-10-18 14:29:42', 'WITHDRAWAL', -100.00, 2250.00, 14),
('2025-10-19 08:06:19', 'DEPOSIT', 200.00, 2450.00, 14),
('2025-10-19 09:41:55', 'WITHDRAWAL', -80.00, 2370.00, 14),
('2025-10-19 10:18:33', 'DEPOSIT', 250.00, 2620.00, 14),
('2025-10-19 11:54:07', 'WITHDRAWAL', -120.00, 2500.00, 14),
('2025-10-19 12:31:48', 'DEPOSIT', 300.00, 2800.00, 14),
('2025-10-19 13:07:22', 'WITHDRAWAL', -60.00, 2740.00, 14),
-- Client 13 (account_id 15)
('2025-10-16 07:22:39', 'DEPOSIT', 300.00, 900.00, 15),
('2025-10-16 10:58:14', 'WITHDRAWAL', -100.00, 800.00, 15),
('2025-10-17 07:35:47', 'DEPOSIT', 200.00, 1000.00, 15),
('2025-10-17 11:11:21', 'WITHDRAWAL', -50.00, 950.00, 15),
('2025-10-18 07:48:56', 'DEPOSIT', 400.00, 1350.00, 15),
('2025-10-18 12:24:32', 'WITHDRAWAL', -150.00, 1200.00, 15),
('2025-10-19 07:51:08', 'DEPOSIT', 250.00, 1450.00, 15),
('2025-10-19 09:17:43', 'WITHDRAWAL', -80.00, 1370.00, 15),
('2025-10-19 10:44:19', 'DEPOSIT', 350.00, 1720.00, 15),
('2025-10-19 11:29:54', 'WITHDRAWAL', -60.00, 1660.00, 15),
-- Client 14 (account_id 16)
('2025-10-17 06:37:18', 'DEPOSIT', 400.00, 1700.00, 16),
('2025-10-17 11:14:52', 'WITHDRAWAL', -150.00, 1550.00, 16),
('2025-10-18 06:51:27', 'DEPOSIT', 350.00, 1900.00, 16),
('2025-10-18 10:28:03', 'WITHDRAWAL', -100.00, 1800.00, 16),
('2025-10-18 16:04:39', 'DEPOSIT', 200.00, 2000.00, 16),
('2025-10-19 06:41:14', 'WITHDRAWAL', -80.00, 1920.00, 16),
('2025-10-19 08:17:50', 'DEPOSIT', 250.00, 2170.00, 16),
('2025-10-19 09:54:26', 'WITHDRAWAL', -120.00, 2050.00, 16),
('2025-10-19 10:31:01', 'DEPOSIT', 300.00, 2350.00, 16),
('2025-10-19 11:07:37', 'WITHDRAWAL', -60.00, 2290.00, 16),
-- Client 15 (account_id 17)
('2025-10-15 05:49:27', 'DEPOSIT', 300.00, 1000.00, 17),
('2025-10-15 10:26:03', 'WITHDRAWAL', -100.00, 900.00, 17),
('2025-10-16 05:02:38', 'DEPOSIT', 200.00, 1100.00, 17),
('2025-10-16 11:39:14', 'WITHDRAWAL', -50.00, 1050.00, 17),
('2025-10-17 05:15:49', 'DEPOSIT', 400.00, 1450.00, 17),
('2025-10-17 14:52:25', 'WITHDRAWAL', -150.00, 1300.00, 17),
('2025-10-18 05:29:00', 'DEPOSIT', 250.00, 1550.00, 17),
('2025-10-18 13:05:36', 'WITHDRAWAL', -80.00, 1470.00, 17),
('2025-10-19 05:42:11', 'DEPOSIT', 350.00, 1820.00, 17),
('2025-10-19 08:18:47', 'WITHDRAWAL', -60.00, 1760.00, 17),
-- Client 16 (account_id 18)
('2025-10-17 04:16:45', 'DEPOSIT', 400.00, 2000.00, 18),
('2025-10-17 10:53:21', 'WITHDRAWAL', -150.00, 1850.00, 18),
('2025-10-18 04:29:56', 'DEPOSIT', 350.00, 2200.00, 18),
('2025-10-18 09:06:32', 'WITHDRAWAL', -100.00, 2100.00, 18),
('2025-10-18 16:43:08', 'DEPOSIT', 200.00, 2300.00, 18),
('2025-10-19 04:19:43', 'WITHDRAWAL', -80.00, 2220.00, 18),
('2025-10-19 07:56:19', 'DEPOSIT', 250.00, 2470.00, 18),
('2025-10-19 09:32:54', 'WITHDRAWAL', -120.00, 2350.00, 18),
('2025-10-19 11:09:30', 'DEPOSIT', 300.00, 2650.00, 18),
('2025-10-19 12:46:05', 'WITHDRAWAL', -60.00, 2590.00, 18),
-- Client 17 (account_id 19)
('2025-10-16 03:28:33', 'DEPOSIT', 300.00, 1200.00, 19),
('2025-10-16 09:05:09', 'WITHDRAWAL', -100.00, 1100.00, 19),
('2025-10-17 03:41:44', 'DEPOSIT', 200.00, 1300.00, 19),
('2025-10-17 11:18:20', 'WITHDRAWAL', -50.00, 1250.00, 19),
('2025-10-18 03:54:55', 'DEPOSIT', 400.00, 1650.00, 19),
('2025-10-18 10:31:31', 'WITHDRAWAL', -150.00, 1500.00, 19),
('2025-10-19 03:08:06', 'DEPOSIT', 250.00, 1750.00, 19),
('2025-10-19 08:44:42', 'WITHDRAWAL', -80.00, 1670.00, 19),
('2025-10-19 10:21:17', 'DEPOSIT', 350.00, 2020.00, 19),
('2025-10-19 11:57:53', 'WITHDRAWAL', -60.00, 1960.00, 19),
-- Client 18 (account_id 20)
('2025-10-18 02:41:08', 'DEPOSIT', 400.00, 1800.00, 20),
('2025-10-18 08:17:44', 'WITHDRAWAL', -150.00, 1650.00, 20),
('2025-10-18 12:54:19', 'DEPOSIT', 350.00, 2000.00, 20),
('2025-10-18 18:30:55', 'WITHDRAWAL', -100.00, 1900.00, 20),
('2025-10-19 02:07:30', 'DEPOSIT', 200.00, 2100.00, 20),
('2025-10-19 08:44:06', 'WITHDRAWAL', -80.00, 2020.00, 20),
('2025-10-19 10:20:41', 'DEPOSIT', 250.00, 2270.00, 20),
('2025-10-19 11:57:17', 'WITHDRAWAL', -120.00, 2150.00, 20),
('2025-10-19 12:33:52', 'DEPOSIT', 300.00, 2450.00, 20),
('2025-10-19 13:10:28', 'WITHDRAWAL', -60.00, 2390.00, 20);