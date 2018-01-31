package vn.com.la.service.dto;

public class QualitiDTO {
    private String retoucher;
    private String qc;
    private Long volumn;
    private Long error;
    private Double error_rate;

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



    public Double getError_rate() {
        return error_rate;
    }

    public void setError_rate(Double error_rate) {
        this.error_rate = error_rate;
    }

}
