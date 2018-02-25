package vn.com.la.web.rest.vm.response;

/**
 * Created by LenVo on 2/21/18.
 */
public class ReportXlsResponseVM extends AbstractResponseVM{
    private String fileName;
    private byte[] bytes;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
