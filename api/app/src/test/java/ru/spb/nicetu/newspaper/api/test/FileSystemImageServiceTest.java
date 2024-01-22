package ru.spb.nicetu.newspaper.api.test;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import io.jsonwebtoken.lang.Assert;
import ru.spb.nicetu.newspaper.api.environment.FileSystemImageServiceTestEnvironment;
import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;

@SpringBootTest
public class FileSystemImageServiceTest {
    private final FileSystemImageServiceTestEnvironment env;

    @Autowired
    public FileSystemImageServiceTest(FileSystemImageServiceTestEnvironment env) {
        this.env = env;
    }

    @Test
    public void complexTest() throws IOException {
        AbstractImage image = env.getImageService().saveImage(
            env.getTestFile(),
            env.getContentType(),
            env.getTestNews().getId()
        );

        AbstractImage dbImage = env.getImageService().findImage(env.getTestNews().getId());

        Assert.isTrue(dbImage != null, "Image must be present");
        Assert.isTrue(image.equals(dbImage), "Images must be equal");

        Resource imageResource = dbImage.asResource();

        Assert.isTrue(imageResource.exists(), "Resource must exist");
        Assert.isTrue(imageResource.isFile(), "Resource must be backed by file");
        Assert.isTrue(imageResource.isReadable(), "File must be readable");

        env.getImageService().deleteImage(env.getTestNews().getId());

        dbImage = env.getImageService().findImage(env.getTestNews().getId());

        Assert.isNull(dbImage, "Image must not be persistent");
        Assert.isTrue(!Files.exists(imageResource.getFile().toPath()), "File must not be present");
    }
}
