package edu.pjatk.app.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;


@Repository
public class ReportRepository {

    private final EntityManager entityManager;

    @Autowired
    public ReportRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Report report) {
        entityManager.persist(report);
    }

    public void remove(Report report) {
        entityManager.remove(report);
    }

    public void update(Report report) {
        entityManager.merge(report);
    }

    public Optional<Report> getReport(Long id) {
        Optional<Report> report;
        try {
            report = Optional.of(
                    entityManager.createQuery("SELECT r FROM Report r WHERE r.id=:id", Report.class)
                            .setParameter("id", id).getSingleResult()
                    );
        } catch (NoResultException e) {
            report = Optional.empty();
        }
        return report;
    }

    public Optional<List<Report>> getReports() {
        Optional<List<Report>> reports;
        try {
            reports = Optional.of(
                    entityManager.createQuery("SELECT report from Report report", Report.class).getResultList()
                    );
        } catch (NoResultException e) {
            reports = Optional.empty();
        }
        return reports;
    }

    public Optional<Report> getByEntityTypeAndIdAndUserId(EntityTypeEnum entityType, Long entityId, Long userId) {
        Optional<Report> report;
        try {
            report = Optional.of(
                    entityManager.createQuery(
                                    "SELECT report FROM Report report WHERE " +
                                            "report.entityType =: entityType AND" +
                                            " report.entityId =: entityId AND" +
                                            " report.userId =: userId", Report.class
                            ).setParameter("entityType", entityType).setParameter("entityId", entityId).
                            setParameter("userId", userId).getSingleResult()
            );
        } catch (NoResultException e) {
            report = Optional.empty();
        }
        return report;
    }

}
