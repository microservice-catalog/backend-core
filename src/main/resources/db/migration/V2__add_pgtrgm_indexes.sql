CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_project_info_title_trgm
    ON project_info USING gin (lower(title) gin_trgm_ops);

CREATE INDEX idx_project_info_description_trgm
    ON project_info USING gin (lower(description) gin_trgm_ops);
