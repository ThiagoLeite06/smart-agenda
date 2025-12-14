-- Convert enum columns to VARCHAR for better Hibernate compatibility

-- Drop functions that depend on the enum types
DROP FUNCTION IF EXISTS get_patient_consultation_history(BIGINT);
DROP FUNCTION IF EXISTS get_future_consultations(BIGINT);

-- Remove default value from consultation.status
ALTER TABLE consultation ALTER COLUMN status DROP DEFAULT;

-- Convert doctor.specialty from enum to VARCHAR
ALTER TABLE doctor
    ALTER COLUMN specialty TYPE VARCHAR(50);

-- Convert consultation.status from enum to VARCHAR
ALTER TABLE consultation
    ALTER COLUMN status TYPE VARCHAR(50);

-- Set default value back for consultation.status
ALTER TABLE consultation ALTER COLUMN status SET DEFAULT 'SCHEDULED';

-- Drop the enum types (CASCADE to handle any remaining dependencies)
DROP TYPE IF EXISTS specialty CASCADE;
DROP TYPE IF EXISTS status CASCADE;
