package hu.spa.controller;

import hu.spa.model.domain.Department;
import hu.spa.model.domain.GuestLog;
import hu.spa.model.domain.LogTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpaService {

    private final List<GuestLog> guestLogs;

    public SpaService(List<GuestLog> guestLogs) {
        this.guestLogs = guestLogs;
    }

    /**
     * 2. feladat
     */
    public String getFirstAndLastGuestTime() {
        int firstGuestId= guestLogs.get(0).getGuestId();
        int lastGuestId= guestLogs.get(guestLogs.size() - 1).getGuestId();
        return String.format("Az első vendég %s-kor lépett ki az öltözőből.\nAz utolsó vendég %s-kor lépett ki az öltözőből.",
                getArrivalTime(firstGuestId), getArrivalTime(lastGuestId));
    }

    private LogTime getArrivalTime(int guestId) {
        return guestLogs.stream()
                .filter(guestLog -> guestLog.getGuestId() == guestId)
                .map(GuestLog::getArrivalTime)
                .findAny()
                .get();
    }

    /**
     * 3. feladat
     */
    public Long countSingleDepartmentVisit() {
        return guestLogs.stream()
                .filter(GuestLog::isSingleDepartmentVisit)
                .count();
    }

    /**
     * 4. feladat
     */
    public String getLongestStayGuestDetail() {
        GuestLog longestStayGuest = getLongestStayGuest();
        return String.format("%d. vendég %s", longestStayGuest.getGuestId(), longestStayGuest.getStayTime());
    }

    private GuestLog getLongestStayGuest() {
        return guestLogs.stream()
                .max(GuestLog::compareTo)
                .get();
    }

    /**
     * 5. feladat
     */
    public String getGuestStatisticByHour() {
        return getGuestStatisticByHour(6, 9) + getGuestStatisticByHour(9, 16) + getGuestStatisticByHour(16, 20);
    }

    private String getGuestStatisticByHour(int from, int to) {
        return String.format("%d-%d óra között %d vendég%n", from, to, countGuestByArrivalHour(from, to));
    }

    private Long countGuestByArrivalHour(int from, int to) {
        return guestLogs.stream()
                .filter(guestLog -> guestLog.isArrivalHour(from, to))
                .count();
    }

    /**
     * 6. feladat
     */
    public List<String> getTotalTimeInSauna() {
        Department saunas = Department.SAUNAS;
        return guestLogs.stream()
                .filter(guestLog -> guestLog.hasStayTime(saunas))
                .map(i -> i.getGuestId() + " " + i.getStayTime(saunas))
                .collect(Collectors.toList());
    }

    /**
     * 7. feladat
     */

    public String getDepartmentVisitStatistic() {
        return count().entrySet().stream()
                .filter(i -> i.getKey() != Department.DRESSING_ROOM)
                .map(i -> i.getKey().getDescription() + ": " + i.getValue())
                .collect(Collectors.joining("\r\n"));
    }

    private Map<Department, Integer> count() {
        Map<Department, Integer> departmentIntegerMap = new HashMap<>();
        for(var guestLog : guestLogs) {
            Map<Department, Boolean> departmentVisitMap = guestLog.getDepartmentVisitMap();
            for(var departmentVisitEntrySet : departmentVisitMap.entrySet()) {
                if (departmentVisitEntrySet.getValue()) {
                    Department key = departmentVisitEntrySet.getKey();
                    int value = getValue(departmentIntegerMap, key);
                    departmentIntegerMap.put(key, value);
                }
            }
        }
        return departmentIntegerMap;
    }

    private int getValue(Map<Department, Integer> departmentIntegerMap, Department department) {
        return Optional.of(departmentIntegerMap)
                .map(map  -> map.get(department))
                .orElse(0) + 1;
    }
}
