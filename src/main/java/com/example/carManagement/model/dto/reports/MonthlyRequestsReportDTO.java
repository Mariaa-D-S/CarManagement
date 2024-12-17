package com.example.carManagement.model.dto.reports;

import java.time.YearMonth;

public class MonthlyRequestsReportDTO {
    private YearMonth yearMonth;
    private Integer requests;

    public MonthlyRequestsReportDTO(YearMonth yearMonth, Integer requests) {
        this.yearMonth = yearMonth;
        this.requests = requests;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }
}
