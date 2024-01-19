package com.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.entity.FileData;
import com.demo.entity.ImageData;
import com.demo.respository.FileDataRepository;
import com.demo.respository.StorageRepository;
import com.demo.util.ImageUtils;

@Service
public class StorageService {

	@Autowired
	private StorageRepository repository;

	@Autowired
	private FileDataRepository fileDataRepository;

//    private final String FOLDER_PATH="/Users/javatechie/Desktop/MyFIles/";
	private final String FOLDER_PATH = "C:\\Users\\shiva\\OneDrive\\Desktop\\path\\";

	@Value("${folder.path}")
	private String folderPath;

	public String uploadImage(MultipartFile file) throws IOException {
		ImageData imageData = repository.save(ImageData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).imageData(ImageUtils.compressImage(file.getBytes())).build());
		if (imageData != null) {
			return "file uploaded successfully : " + file.getOriginalFilename();
		}
		return null;
	}

	public byte[] downloadImage(String fileName) {
		Optional<ImageData> dbImageData = repository.findByName(fileName);
		byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
		return images;
	}

	public String uploadImageToFileSystem(MultipartFile file) throws IOException {
		String filePath = folderPath + file.getOriginalFilename();

		FileData fileData = fileDataRepository.save(FileData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).filePath(filePath).build());

		file.transferTo(new File(filePath));

		if (fileData != null) {
			return "file uploaded successfully : " + filePath;
		}
		return null;
	}

	public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
		Optional<FileData> fileData = fileDataRepository.findByName(fileName);
		String filePath = fileData.get().getFilePath();
		byte[] images = Files.readAllBytes(new File(filePath).toPath());
		return images;
	}

	public String uploadImageToLocalSystem(MultipartFile file) throws IOException {
		String filePath = folderPath + file.getOriginalFilename();

		FileData fileData = FileData.builder()
				.name(file.getOriginalFilename())
				.type(file.getContentType())
				.filePath(filePath).build();

		file.transferTo(new File(filePath));

		if (fileData != null) {
			return "file uploaded local system successfully : " + filePath;
		}
		return null;
	}

}
