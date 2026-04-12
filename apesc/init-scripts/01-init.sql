-- Garante que o banco existe e configura as permissões
\c postgres

-- Garante que o usuário admin tem todas as permissões
GRANT ALL PRIVILEGES ON DATABASE apesc_db TO admin;

-- Conecta ao banco da aplicação
\c apesc_db

-- Garante permissões no esquema público
GRANT ALL ON SCHEMA public TO admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO admin;
