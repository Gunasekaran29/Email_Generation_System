-- Run in: Supabase Dashboard → SQL Editor → New Query

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS emails (
  id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  sender       TEXT        NOT NULL,
  sender_email TEXT        NOT NULL,
  recipients   TEXT[]      NOT NULL DEFAULT '{}',
  subject      TEXT        NOT NULL,
  body         TEXT,
  preview      TEXT,
  status       TEXT        NOT NULL DEFAULT 'inbox',   -- inbox | sent | trash
  is_read      BOOLEAN     NOT NULL DEFAULT FALSE,
  is_starred   BOOLEAN     NOT NULL DEFAULT FALSE,
  avatar       TEXT        DEFAULT '',
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_emails_status     ON emails (status);
CREATE INDEX IF NOT EXISTS idx_emails_created_at ON emails (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_emails_starred    ON emails (is_starred) WHERE is_starred = TRUE;
