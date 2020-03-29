package hu.spa.model.domain;

public class DepartmentLog {

    private final Department department;
    private final LogTime arrivalTime;
    private final LogTime departureTime;

    public DepartmentLog(Department department, LogTime arrivalTime, LogTime departureTime) {
        this.department = department;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    public Department getDepartment() {
        return department;
    }

    public LogTime getArrivalTime() {
        return arrivalTime;
    }

    public LogTime getDepartureTime() {
        return departureTime;
    }

    public LogTime getStayTime() {
        return LogTime.diff(arrivalTime, departureTime);
    }
}
