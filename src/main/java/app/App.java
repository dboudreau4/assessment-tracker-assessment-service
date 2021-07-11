package app;

import dao.AssessmentDAOImpl;
import dao.AssessmentTypeDAOImpl;
import io.javalin.Javalin;
import controllers.AssessmentController;
import controllers.CategoryController;
import dao.CategoryDAOImpl;
import models.AssessmentType;
import services.*;

public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.enableDevLogging();
        });
        establishRoutes(app);
        app.start(7001);
    }

    private static void establishRoutes(Javalin app) {
        // Need a Repo
        // AssessmentRepo ar= new AssessmentRepo();
        // Need a Service
        CategoryController categoryController = new CategoryController(new CategoryServiceImpl(new CategoryDAOImpl()));
        AssessmentService as = new AssessmentServiceImpl(new AssessmentDAOImpl());
        AssessmentTypeService ats = new AssessmentTypeServiceImpl(new AssessmentTypeDAOImpl());
        app.get("/Testing", context -> context.result("Testing"));

        // EndPoints
        AssessmentController ac = new AssessmentController(as, ats);
        app.get("/assessments", ac.getAssessments);
        app.post("/assessments", ac.createAssessment);
        app.get("/assessments/:id/", ac.getAssessmentsByTraineeId);
        app.get("/assessments/batch/:id/:weekid", ac.getBatchWeek);
        app.put("/assessments/weight/:assessmentId/:weight", ac.adjustWeight);
        app.put("/assessments/type/:assessmentId/:typeId",ac.assignAssessmentType);

        app.get("/grade/:associateId/:assessmentId", ac.getGradeForAssociate);
        app.get("/grades/:id/:weekid", ac.getGradesForWeek);
        app.put("/grades/", ac.insertGrade);

        app.get("/notes/:id/:weekid/", ac.getNotesForTrainee);

        app.post("/types", ac.createAssessmentType);


        app.get("/categories", categoryController.getCategories);
        app.post("/category", categoryController.createCategory);
        app.get("/category/:id", categoryController.getCategoryById);
        app.patch("/category", categoryController.updateCategory);
        app.delete("/category/:id", categoryController.deleteCategory);
    }

}
