CREATE FUNCTION `nextval` (`seq_name` VARCHAR(100))
    RETURNS BIGINT(20) NOT DETERMINISTIC
BEGIN
    UPDATE SequenceData SET sequenceCurValue = (@next := sequenceCurValue + sequenceIncrement) WHERE sequenceName = seq_name;
    RETURN @next;
END;
