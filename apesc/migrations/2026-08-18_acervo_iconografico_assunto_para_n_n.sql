-- ============================================================================
-- Migração: acervo_iconografico -> acervo_iconografico_assuntos era 1:N
-- (assunto_id na própria tabela), vira N:N de verdade via tabela associativa
-- acervo_iconografico_assunto_vinculo (nome de propósito diferente do catálogo
-- "acervo_iconografico_assuntos", pra não confundir singular/plural).
--
-- Sem dado a preservar: acervo_iconografico está vazia (criada nesta sessão).
-- ============================================================================

BEGIN;

CREATE TABLE IF NOT EXISTS acervo_iconografico_assunto_vinculo (
    acervo_iconografico_id BIGINT NOT NULL REFERENCES acervo_iconografico(id),
    assunto_id BIGINT NOT NULL REFERENCES acervo_iconografico_assuntos(id),
    PRIMARY KEY (acervo_iconografico_id, assunto_id)
);

ALTER TABLE acervo_iconografico DROP COLUMN IF EXISTS assunto_id;

COMMIT;

-- ============================================================================
-- Verificação pós-migração (opcional, rode manualmente):
-- \d acervo_iconografico
-- \d acervo_iconografico_assunto_vinculo
-- ============================================================================
