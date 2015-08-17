package com.mistno.minewrap.backup;

import static com.mistno.minewrap.State.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.text.DateFormat;
import java.util.*;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.mistno.minewrap.StateHolder;
import com.mistno.minewrap.config.Config;

public class ZipBackup extends AbstractBackup {

	private final StateHolder stateHolder;

	private Set<String> exludedFilesAndFolders = new HashSet<>();

	public ZipBackup(StateHolder stateHolder) {
		this.stateHolder = stateHolder;
		for (String string : Config.get("backup.zip.exclude").replaceAll(" ", "").split(",")) {
			exludedFilesAndFolders.add(string);
		}
	}

	@Override
	public void performBackup() {
		System.out.println("Backup process started...");

		String dateTimeString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()).replace(":", "").replace(" ", "_").replace("-", "");
		File target = new File(Config.get("backup.zip.targetDir") + "/minewrap_backup_" + dateTimeString + ".zip");
		Path source = Paths.get(Config.get("backup.zip.sourceDir"));
		try {
			validateSource(source);
			ArchiveOutputStream stream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new FileOutputStream(target));
			Collection<Path> files = getFilesInDirectory(source);
			ByteBuffer buffer = ByteBuffer.allocate(100 * 1000 * 1000);

			for (Path file : files) {
				stream.putArchiveEntry(getEntryForFile(source, file));
				readFileToByteBuffer(file, buffer);
				stream.write(buffer.array(), 0, buffer.limit());
				stream.closeArchiveEntry();
				buffer.clear();
			}

			stream.flush();
			stream.close();
			stateHolder.setState(RUN);
			System.out.println("Backup process finished!");
		} catch (IOException | ArchiveException | IllegalStateException e) {
			stateHolder.setState(END);
			e.printStackTrace();
		}
	}

	private void validateSource(Path source) {
		if (Files.notExists(source, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(source, LinkOption.NOFOLLOW_LINKS)) {
			throw new IllegalStateException(Config.get("backup.zip.sourceDir") + " didn't exist or wasn't a directory");
		}
	}

	private ArchiveEntry getEntryForFile(Path source, Path file) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(file.toFile(), source.relativize(file).toString());
		entry.setSize(Files.size(file));
		return entry;
	}

	@SuppressWarnings("resource")
	private void readFileToByteBuffer(Path file, ByteBuffer buffer) throws IOException {
		FileChannel channel = new FileInputStream(file.toFile()).getChannel();
		channel.read(buffer);
		channel.close();
		buffer.flip();
	}

	private Collection<Path> getFilesInDirectory(Path rootDirectory) throws IOException {
		Collection<Path> fileCollection = new ArrayList<>();
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootDirectory);
		for (Path path : directoryStream) {
			if (exludedFilesAndFolders.contains(path.getFileName().toString())) {
				continue;
			}

			if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
				fileCollection.addAll(getFilesInDirectory(path));
			} else {
				fileCollection.add(path);
			}
		}
		return fileCollection;
	}
}
