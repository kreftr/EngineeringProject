package edu.pjatk.app.report;

import edu.pjatk.app.request.ReportRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReportResultEnum report(ReportRequest reportRequest) {
        Optional<User> loggedUser = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Report> alreadyReport = reportRepository.getByEntityTypeAndIdAndUserId(
                EntityTypeEnum.stringToEnum(reportRequest.getEntityType()), reportRequest.getEntityId(), loggedUser.get().getId()
        );
        if (alreadyReport.isEmpty()) {
            try {
                reportRepository.create(
                        new Report(loggedUser.get().getId(), EntityTypeEnum.stringToEnum(reportRequest.getEntityType()),
                                reportRequest.getEntityId(), LocalDateTime.now(), reportRequest.getReasoning())
                );
                return ReportResultEnum.REPORTED;
            } catch (Exception e) {
                return ReportResultEnum.ERROR;
            }
        } else {
            return ReportResultEnum.ALREADY_REPORTED;
        }
    }

    public List<Report> getReports() {
        Optional<List<Report>> reports = reportRepository.getReports();
        if (reports.isPresent() && !reports.get().isEmpty()) {
            return reports.get();
        }
        else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public ReportResultEnum remove(Long id) {
        Optional<Report> report = reportRepository.getReport(id);
        if (report.isPresent()) {
            reportRepository.remove(report.get());
            return ReportResultEnum.REMOVED;
        } else {
            return ReportResultEnum.NOT_FOUND;
        }
    }

}
