package edu.pjatk.app.recomendations;

import edu.pjatk.app.project.Project;
import edu.pjatk.app.project.ProjectRepository;
import edu.pjatk.app.project.category.Category;
import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendationService {

    private final ProjectRepository projectRepository;

    @Autowired
    public RecomendationService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public String monthlyRecomendations(User user) {
        Optional<List<Project>> monthlyProjects = projectRepository.getAllFromLastMonth();
        Set<Category> userCategories = user.getProfile().getCategories();

        TreeMap<Long, Integer> commonCategories = new TreeMap<>();
        for (Project project: monthlyProjects.get())
        {
            int commonCategoryCount = Collections.frequency(userCategories, project.getCategories());
            commonCategories.put(project.getId(), commonCategoryCount);
        }

        // sort common categories in reverse order
        LinkedHashMap<Long, Integer> commonCategoriesSorted = commonCategories.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        // gets first key from map sorted by values in reverse
        Long recommendedProjectId = commonCategoriesSorted.keySet().stream().findFirst().get();
        return "http://localhost:3000/project/" + recommendedProjectId;
    }

}
