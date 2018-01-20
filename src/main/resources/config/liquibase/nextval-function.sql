CREATE FUNCTION `nextval` (`seq_name` VARCHAR(100))
    RETURNS BIGINT(20) NOT DETERMINISTIC
BEGIN
    UPDATE sequence_data SET sequence_cur_value = (@next := sequence_cur_value + sequence_increment) WHERE sequence_name = seq_name;
    RETURN @next;
END;
