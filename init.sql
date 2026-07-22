CREATE SCHEMA IF NOT EXISTS auth;

-- 1. Criar o ENUM de forma segura
-- DO $$ 
--BEGIN
--    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role') THEN
--        CREATE TYPE user_role AS ENUM ('ROLE_USER', 'ROLE_ADMIN');
--    END IF;
-- END $$;

-- 2. Criar a tabela pública de perfis (independente e sem chave estrangeira rígida para o init)
-- CREATE TABLE IF NOT EXISTS public.perfis (
--    id UUID PRIMARY KEY,
--    nome VARCHAR(255),
--    perfil user_role DEFAULT 'ROLE_USER'::user_role NOT NULL,
--    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
--);