package ru.spb.nicetu.newspaper.api.model.image;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>{@code AbstractImage} implementation using file system to store picture content.</p>
 *
 * <p>Contains relative file system path of stored image.</p>
    * @author Nikita Osiptsov
    * @see {@link AbstractImage}
 * @since 1.0
 */
@Entity
@Table(name = "image", schema = "subject")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FileSystemImage extends AbstractImage {
    @Column(name = "path")
    private String path;

    @Override
    public Resource asResource() {
        return new FileSystemResource(path);
    }

    @Override
    @PreRemove
    public void breakAssociation() {
        super.breakAssociation();
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch(Exception e) {
        }
    }
    
}
