package edu.pjatk.app.report.blockade;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectAccess;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.report.EntityTypeEnum;
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

    @Autowired
    public BlockadeService(BlockadeRepository blockadeRepository, UserService userService,
                           ProjectRepository projectRepository) {
        this.blockadeRepository = blockadeRepository;
        this.userService  = userService;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public BlockadeResultEnum block(BlockRequest request) {

        if (blockadeRepository.findByEntityTypeAndEntityIdAndUserId(
                request.getEntityType(), request.getEntityId(), request.getUserId()).isPresent()
        ) {
            return BlockadeResultEnum.ALREADY_BLOCKED;
        } else {
            if (EntityTypeEnum.PROJECT.equals(request.getEntityType())) {
                Optional<Project> project = projectRepository.getProjectById(request.getEntityId());
                project.get().setProject_access(ProjectAccess.BLOCKED);
                projectRepository.update(project.get());
            }
            Optional<User> user = userService.findUserById(request.getUserId());
            user.get().setLocked(true);
            userService.updateUser(user.get());

            blockadeRepository.add(new Blockade(
                    request.getUserId(), request.getEntityType(), request.getEntityId(), LocalDateTime.now(),
                    LocalDateTime.now().plusDays(request.getDaysOfBlockade()), request.getReasoning()
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
