package vn.com.la.domain;

import javax.persistence.*;

@Entity
@Table(name = "SequenceData")
public class SequenceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequenceName", nullable = false, length = 100)
    private String sequenceName;

    @Column(name = "sequenceIncrement", nullable = false, length = 10)
    private int sequenceIncrement;

    @Column(name = "sequenceMinValue", nullable = false, length = 10)
    private int sequenceMinValue;

    @Column(name = "sequenceMaxValue", nullable = false, length = 19)
    private long sequenceMaxValue;

    @Column(name = "sequenceCurValue", nullable = true, length = 19)
    private long sequenceCurValue;

    @Column(name = "sequenceCycle", nullable = false)
    private boolean sequenceCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public int getSequenceIncrement() {
        return sequenceIncrement;
    }

    public void setSequenceIncrement(int sequenceIncrement) {
        this.sequenceIncrement = sequenceIncrement;
    }

    public int getSequenceMinValue() {
        return sequenceMinValue;
    }

    public void setSequenceMinValue(int sequenceMinValue) {
        this.sequenceMinValue = sequenceMinValue;
    }

    public long getSequenceMaxValue() {
        return sequenceMaxValue;
    }

    public void setSequenceMaxValue(long sequenceMaxValue) {
        this.sequenceMaxValue = sequenceMaxValue;
    }

    public long getSequenceCurValue() {
        return sequenceCurValue;
    }

    public void setSequenceCurValue(long sequenceCurValue) {
        this.sequenceCurValue = sequenceCurValue;
    }

    public boolean isSequenceCycle() {
        return sequenceCycle;
    }

    public void setSequenceCycle(boolean sequenceCycle) {
        this.sequenceCycle = sequenceCycle;
    }
}
