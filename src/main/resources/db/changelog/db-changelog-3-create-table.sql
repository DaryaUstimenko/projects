-- Add profile_cover and emoji_status columns, change phone type, and remove status_code and age columns.
ALTER TABLE schema_account.account
    DROP COLUMN IF EXISTS status_code;

ALTER TABLE schema_account.account
    DROP COLUMN IF EXISTS age;

ALTER TABLE schema_account.account
    ALTER COLUMN phone TYPE VARCHAR(20);

ALTER TABLE schema_account.account
    ADD COLUMN profile_cover VARCHAR(255);

ALTER TABLE schema_account.account
    ADD COLUMN emoji_status VARCHAR(255);