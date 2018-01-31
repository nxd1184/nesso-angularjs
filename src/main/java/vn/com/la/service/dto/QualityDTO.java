package vn.com.la.service.dto;

public class QualityDTO {
    private Long retoucherId;
    private String retoucher;
    private Long qcId;
    private String qc;
    private Long volumn;
    private Long error;
    private Double errorRate;

    public String getRetoucher() {
        return retoucher;
    }

    public void setRetoucher(String retoucher) {
        this.retoucher = retoucher;
    }

    public String getQc() {
        return qc;
    }

    public void setQc(String qc) {
        this.qc = qc;
    }

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
    }



    public Long getVolumn() {
        return volumn;
    }

    public void setVolumn(Long volumn) {
        this.volumn = volumn;
    }



    public Double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Double errorRate) {
        this.errorRate = errorRate;
    }

    public Long getRetoucherId() {
        return retoucherId;
    }

    public void setRetoucherId(Long retoucherId) {
        this.retoucherId = retoucherId;
    }

    public Long getQcId() {
        return qcId;
    }

    public void setQcId(Long qcId) {
        this.qcId = qcId;
    }
}
