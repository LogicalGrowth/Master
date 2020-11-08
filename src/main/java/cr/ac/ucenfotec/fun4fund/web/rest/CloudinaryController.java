package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.Image;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ImageRepository;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
import cr.ac.ucenfotec.fun4fund.service.CloudinaryService;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@Transactional
public class CloudinaryController {
    private static final String ENTITY_NAME = "imagen";

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProyectRepository proyectRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @PostMapping("/uploadtoproyect/{id}")
    public ResponseEntity<?> upload(@PathVariable Long id, @RequestParam MultipartFile multipartFile) throws IOException, URISyntaxException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            throw new BadRequestAlertException("No ha subido una imagen", ENTITY_NAME, "idexists");
        }
        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image();
        image.setUrl((String)result.get("url"));
        Proyect proyect = proyectRepository.findById(id).get();
        image.setProyect(proyect);
        imageRepository.save(image);
        return ResponseEntity.created(new URI("/api/proyects/" + image.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, image.getId().toString()))
            .body(result);
    }
}
