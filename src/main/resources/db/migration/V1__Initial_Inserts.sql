CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO member (id, name, email, joindate, membershipstatus) VALUES
(uuid_generate_v4(),'John Doe', 'john@example.com', '2024-01-18', 'ACTIVE');

INSERT INTO Product (id, name, type, price) VALUES
(uuid_generate_v4(), 'Laptop', 'PHYSICAL', 10000.00);

INSERT INTO Product (id, name, type, price) VALUES
(uuid_generate_v4(), 'New Membership', 'MEMBERSHIP_OR_UPGRADE', 200.00);

INSERT INTO Product (id, name, type, price, othersdetails) VALUES
(uuid_generate_v4(), 'Vídeo - Aprendendo a Esquiar', 'VIDEO', 300.00,
'{"bonus": "https://www.youtube.com/watch?v=fsnv8b1vNUY"}');

INSERT INTO agent
(id, contactinfo, name)
VALUES(uuid_generate_v4(), 'Bob - The Agent +1 223 222-2232', 'Bob');