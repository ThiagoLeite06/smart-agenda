-- History Service - Previous Consultations Table

CREATE TABLE previous_consultations (
    id BIGSERIAL PRIMARY KEY,
    consultation_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    patient_name VARCHAR(255) NOT NULL,
    patient_email VARCHAR(100) NOT NULL,
    doctor_id BIGINT NOT NULL,
    doctor_name VARCHAR(255) NOT NULL,
    doctor_specialty VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    scheduled_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT current_timestamp
);

-- Indexes for better query performance
CREATE INDEX idx_prev_consultations_consultation_id ON previous_consultations(consultation_id);
CREATE INDEX idx_prev_consultations_patient_id ON previous_consultations(patient_id);
CREATE INDEX idx_prev_consultations_doctor_id ON previous_consultations(doctor_id);
CREATE INDEX idx_prev_consultations_status ON previous_consultations(status);
CREATE INDEX idx_prev_consultations_scheduled_at ON previous_consultations(scheduled_at);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_previous_consultations_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = current_timestamp;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER previous_consultations_updated_at
    BEFORE UPDATE ON previous_consultations
    FOR EACH ROW
    EXECUTE FUNCTION update_previous_consultations_updated_at();
