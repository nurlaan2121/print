package print.print.dto;

public class InfoForPrint {
    private String firstTimePrint;
    private String lastTimePrint;
    private String currentTime;
    private String successA4;
    private String successA5;
    private String successA6;
    private String UnSuccessA4;
    private String UnSuccessA5;
    private String UnSuccessA6;
    private String UnSuccessUnknown;
    private String unknown;
    private String email;
    private String yesterdayLastTimePrint;

    public InfoForPrint(String firstTimePrint, String lastTimePrint, String currentTime, String successA4, String successA5, String successA6, String unknown) {
        this.firstTimePrint = firstTimePrint;
        this.lastTimePrint = lastTimePrint;
        this.currentTime = currentTime;
        this.successA4 = successA4;
        this.successA5 = successA5;
        this.successA6 = successA6;
        this.unknown = unknown;
    }

    public InfoForPrint() {
    }

    public String getUnSuccessA4() {
        return UnSuccessA4;
    }

    public void setUnSuccessA4(String unSuccessA4) {
        UnSuccessA4 = unSuccessA4;
    }

    public String getUnSuccessA5() {
        return UnSuccessA5;
    }

    public void setUnSuccessA5(String unSuccessA5) {
        UnSuccessA5 = unSuccessA5;
    }

    public String getUnSuccessA6() {
        return UnSuccessA6;
    }

    public void setUnSuccessA6(String unSuccessA6) {
        UnSuccessA6 = unSuccessA6;
    }

    public String getUnUnknown() {
        return UnSuccessUnknown;
    }

    public void setUnUnknown(String unUnknown) {
        UnSuccessUnknown = unUnknown;
    }

    public String getFirstTimePrint() {
        return firstTimePrint;
    }

    public void setFirstTimePrint(String firstTimePrint) {
        this.firstTimePrint = firstTimePrint;
    }

    public String getLastTimePrint() {
        return lastTimePrint;
    }

    public void setLastTimePrint(String lastTimePrint) {
        this.lastTimePrint = lastTimePrint;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getSuccessA4() {
        return successA4;
    }

    public void setSuccessA4(String successA4) {
        this.successA4 = successA4;
    }

    public String getSuccessA5() {
        return successA5;
    }

    public void setSuccessA5(String successA5) {
        this.successA5 = successA5;
    }

    public String getSuccessA6() {
        return successA6;
    }

    public void setSuccessA6(String successA6) {
        this.successA6 = successA6;
    }

    public String getUnknown() {
        return unknown;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getYesterdayLastTimePrint() {
        return yesterdayLastTimePrint;
    }

    public void setYesterdayLastTimePrint(String yesterdayLastTimePrint) {
        this.yesterdayLastTimePrint = yesterdayLastTimePrint;
    }

    @Override
    public String toString() {
        return "InfoForPrint{" +
                "firstTimePrint='" + firstTimePrint + '\'' +
                ", lastTimePrint='" + lastTimePrint + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", successA4='" + successA4 + '\'' +
                ", successA5='" + successA5 + '\'' +
                ", successA6='" + successA6 + '\'' +
                ", UnSuccessA4='" + UnSuccessA4 + '\'' +
                ", UnSuccessA5='" + UnSuccessA5 + '\'' +
                ", UnSuccessA6='" + UnSuccessA6 + '\'' +
                ", UnSuccessUnknown='" + UnSuccessUnknown + '\'' +
                ", unknown='" + unknown + '\'' +
                ", email='" + email + '\'' +
                ", yesterdayLastTimePrint='" + yesterdayLastTimePrint + '\'' +
                '}';
    }
}
