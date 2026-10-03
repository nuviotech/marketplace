package marketplace.admin.api.marketplace_Admin.Implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import marketplace.admin.api.marketplace_Admin.enititys.MediaImages;
import marketplace.admin.api.marketplace_Admin.repository.MediaImagesRepository;
import marketplace.admin.api.marketplace_Admin.service.ImageSaver;

@Service
public class ImageSaverImpl implements ImageSaver{
	@Autowired
	MediaImagesRepository mediaImagesRepository;
	
	@Value("${media.image-folder-path}")
	String MEDIA_IMAGE_FOLDER_PATH;
	@Value("${media.images-base-url}")
	String MEDIA_IMAGE_URL;
	
	Logger logger=LoggerFactory.getLogger(ImageSaverImpl.class);
	
	 public boolean save(MultipartFile file, String filePath, String fileName) {
		boolean flag = false;
		try {
			String path = new ClassPathResource(filePath).getFile().getAbsolutePath();
			Files.copy(file.getInputStream(), Paths.get(path + File.separator + fileName),
					StandardCopyOption.REPLACE_EXISTING);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return flag;
	}
	 
	 public String handleMediaImageUpload(MultipartFile[] files, Model model) {
	        StringBuilder fileNames = new StringBuilder();
	        for (MultipartFile file : files) {
	            try {
	            	if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") ) {
		                String name=UUID.randomUUID().toString().replace("-", "")+".jpg";
			            file.transferTo(new File(MEDIA_IMAGE_FOLDER_PATH +"/"+ name));
		                fileNames.append(name).append(",");
		                MediaImages mi=new MediaImages();
		                mi.setName(name);
		                mediaImagesRepository.save(mi);
	            	}else {
	            		logger.warn("IMAGE_WARN : "+file.getOriginalFilename()+" ("+file.getContentType()+") is not saved.");
	            	}
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        model.addAttribute("UploadedFiles" + fileNames.toString());
	        return "redirect:/marketplace/mediaFiles?page=0";
	    }

	@Override
	public String getMediaImages(int page, int limit, Model model) {
		try {
			Pageable pageable=PageRequest.of(page, limit,Sort.by("createdDtTime").descending());
			Page<MediaImages> images=mediaImagesRepository.findAll(pageable);
			model.addAttribute("mediaImages", images);
			model.addAttribute("baseUrl",MEDIA_IMAGE_URL);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPage", images.getTotalPages() );
			return "UserPages/ImageMediaTable";
		}catch(Exception e) {
			return "UserPages/ImageMediaTable";
		}
	}
	
	@Override
	public String removeMediaFile(int id) {
		try {
			MediaImages mi= mediaImagesRepository.findById(id).get();
			if(mi!=null) {
				File file=new File(MEDIA_IMAGE_FOLDER_PATH +"/"+ mi.getName());
				if(file.exists()) {
					file.delete();
				}else {
					logger.error("media file not exist by id "+id);
				}
			}
			mediaImagesRepository.delete(mi);
			return "deleted";
		}catch(Exception e) {
			e.printStackTrace();
			return "error_exist";
		}
	}

}
