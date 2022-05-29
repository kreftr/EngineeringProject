package edu.pjatk.app.report.blockade;

import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.report.EntityTypeEnum;
import edu.pjatk.app.report.ReportRepository;
import edu.pjatk.app.request.BlockRequest;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BlockadeService {

    private final BlockadeRepository blockadeRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ReportRepository reportRepository;
    private final EmailService emailService;

    @Autowired
    public BlockadeService(BlockadeRepository blockadeRepository, UserService userService,
                           ProjectRepository projectRepository, ReportRepository reportRepository,
                           EmailService emailService) {
        this.blockadeRepository = blockadeRepository;
        this.userService  = userService;
        this.projectRepository = projectRepository;
        this.reportRepository = reportRepository;
        this.emailService = emailService;
    }

    @Transactional
    public BlockadeResultEnum block(BlockRequest request) {

        if (blockadeRepository.findByEntityTypeAndEntityId(
                request.getEntityType(), request.getEntityId()).isPresent()
        ) {
            return BlockadeResultEnum.ALREADY_BLOCKED;
        } else {
            Optional<User> user = null;
            if (EntityTypeEnum.PROJECT.equals(request.getEntityType())) {
                Optional<Project> project = projectRepository.getProjectById(request.getEntityId());
                user = Optional.of(project.get().getCreator());
            }
            if (user == null) user = userService.findUserById(request.getEntityId());
            user.get().setLocked(true);
            userService.updateUser(user.get());

            reportRepository.remove(reportRepository.getReport(request.getReportId()).get());

            blockadeRepository.add(new Blockade(
                    user.get().getId(), request.getEntityType(), request.getEntityId(), LocalDateTime.now(),
                    LocalDateTime.now().plusDays(request.getDaysOfBlockade()), request.getReasoning()
            ));
            emailService.send(user.get().getEmail(), "Account suspension", emailService.emailBanBuilder(
                    user.get().getUsername(), request.getReasoning(),
                    LocalDateTime.now().plusDays(request.getDaysOfBlockade()).toString(), "email_ban_notification.html"
            ));
            return BlockadeResultEnum.BLOCKED;
        }
    }

    public List<Blockade> getAllUsers() {
        Optional<List<Blockade>> blockades = blockadeRepository.findAllUsers();
        if (blockades.isEmpty() && blockades.get().isEmpty()) return Collections.emptyList();
        else return blockades.get();
    }
}
