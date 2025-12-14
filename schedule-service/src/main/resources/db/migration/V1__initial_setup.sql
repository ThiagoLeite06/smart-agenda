-- Simple Smart Agenda Schema

-- Enums
CREATE TYPE status AS ENUM ('SCHEDULED', 'ACCOMPLISHED', 'CANCELLED', 'RESCHEDULED');

CREATE TYPE specialty AS ENUM (
    'CARDIOLOGY',
    'DERMATOLOGY',
    'EMERGENCY_MEDICINE',
    'FAMILY_MEDICINE',
    'GASTROENTEROLOGY',
    'OBSTETRICS_AND_GYNECOLOGY',
    'PEDIATRICS'
);

-- Patient Table (simplified)
CREATE TABLE patient (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp
);

-- Doctor Table
CREATE TABLE doctor (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    specialty specialty NOT NULL,
    crm VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp
);

-- Consultation Table (appointment between patient and doctor)
CREATE TABLE consultation (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    status status NOT NULL DEFAULT 'SCHEDULED',
    scheduled_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    CONSTRAINT fk_consultation_patient FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    CONSTRAINT fk_consultation_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE
);

-- Indexes for better query performance
CREATE INDEX idx_consultation_patient_id ON consultation(patient_id);
CREATE INDEX idx_consultation_doctor_id ON consultation(doctor_id);
CREATE INDEX idx_consultation_scheduled_at ON consultation(scheduled_at);
CREATE INDEX idx_consultation_status ON consultation(status);
CREATE INDEX idx_patient_email ON patient(email);
CREATE INDEX idx_doctor_email ON doctor(email);
CREATE INDEX idx_doctor_specialty ON doctor(specialty);

-- Function to get patient consultation history
CREATE OR REPLACE FUNCTION get_patient_consultation_history(p_patient_id BIGINT)
RETURNS TABLE (
    consultation_id BIGINT,
    doctor_id BIGINT,
    doctor_name VARCHAR,
    specialty specialty,
    status status,
    scheduled_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    notes TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.doctor_id,
        d.full_name,
        d.specialty,
        c.status,
        c.scheduled_at,
        c.completed_at,
        c.notes
    FROM consultation c
    JOIN doctor d ON c.doctor_id = d.id
    WHERE c.patient_id = p_patient_id
    ORDER BY c.scheduled_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to get future consultations for a patient
CREATE OR REPLACE FUNCTION get_future_consultations(p_patient_id BIGINT)
RETURNS TABLE (
    consultation_id BIGINT,
    doctor_id BIGINT,
    doctor_name VARCHAR,
    specialty specialty,
    status status,
    scheduled_at TIMESTAMPTZ
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.doctor_id,
        d.full_name,
        d.specialty,
        c.status,
        c.scheduled_at
    FROM consultation c
    JOIN doctor d ON c.doctor_id = d.id
    WHERE c.patient_id = p_patient_id
      AND c.scheduled_at > now()
      AND c.status = 'SCHEDULED'
    ORDER BY c.scheduled_at;
END;
$$ LANGUAGE plpgsql;
