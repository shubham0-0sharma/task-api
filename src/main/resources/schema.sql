DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    CONSTRAINT task_status_check CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'))
);

-- Create index for performance optimization
CREATE INDEX idx_task_status ON tasks(status);
CREATE INDEX idx_task_title ON tasks(title);

-- Add comments for documentation
COMMENT ON TABLE tasks IS 'Stores task information';
COMMENT ON COLUMN tasks.id IS 'Unique identifier for the task';
COMMENT ON COLUMN tasks.title IS 'Title of the task';
COMMENT ON COLUMN tasks.description IS 'Detailed description of the task';
COMMENT ON COLUMN tasks.status IS 'Current status of the task: PENDING, IN_PROGRESS, or COMPLETED';
COMMENT ON COLUMN tasks.created_at IS 'Timestamp when the task was created';
COMMENT ON COLUMN tasks.updated_at IS 'Timestamp when the task was last updated';
COMMENT ON COLUMN tasks.due_date IS 'Deadline for the task completion';