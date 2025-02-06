package patterns;
class Report{
    int reportId;
    String reportName;
    String reportContent;
    String reportLocation;

    public Report(int reportId, String reportName, String reportContent, String reportLocation) {
        this.reportId = reportId;
        this.reportName = reportName;
        this.reportContent = reportContent;
        this.reportLocation = reportLocation;
    }
}

class ReportBuilder{
    int reportId;
    String reportName;
    String reportContent;
    String reportLocation;

    public ReportBuilder setReportId(int reportId) {
        this.reportId = reportId;
        return this;
    }
    public ReportBuilder setReportName(String reportName) {
        this.reportName = reportName;
        return this;
    }
    public ReportBuilder setReportContent(String reportContent) {
        this.reportContent = reportContent;
        return this;
    }
    public ReportBuilder setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
        return this;
    }

    public Report build() {
        return new Report(reportId, reportName, reportContent, reportLocation);
    }

}

public class BuilderImpl {
    public static void main(String[] args) {
        Report report = new ReportBuilder().setReportId(123).setReportContent("Lorem Ipson").setReportLocation("/scratch").setReportName("Demo").build();
        System.out.println(report.reportId);
    }
}
