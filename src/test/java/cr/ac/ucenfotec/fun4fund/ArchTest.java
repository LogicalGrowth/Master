package cr.ac.ucenfotec.fun4fund;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("cr.ac.ucenfotec.fun4fund");

        noClasses()
            .that()
                .resideInAnyPackage("cr.ac.ucenfotec.fun4fund.service..")
            .or()
                .resideInAnyPackage("cr.ac.ucenfotec.fun4fund.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..cr.ac.ucenfotec.fun4fund.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
