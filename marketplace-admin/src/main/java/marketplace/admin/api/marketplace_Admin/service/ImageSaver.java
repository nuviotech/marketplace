package marketplace.admin.api.marketplace_Admin.service;


import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface ImageSaver {
	public boolean save(MultipartFile file, String filePath, String fileName);
	public String handleMediaImageUpload(MultipartFile[] files, Model model);
	public String getMediaImages(int page,int limit, Model model);
	public String removeMediaFile(int id);
}
