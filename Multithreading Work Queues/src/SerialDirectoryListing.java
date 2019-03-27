import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generates a directory listing using single-threading.
 */
public class SerialDirectoryListing {

	/** Logger to use for this class. */
	private static final Logger log = LogManager.getLogger();

	/**
	 * Returns a directory listing for the given path. The original path and all
	 * directories are included in the returned set of paths.
	 *
	 * @param path directory to create listing
	 * @return paths found within directory and its subdirectories
	 */
	public static Set<Path> list(Path path) {
		HashSet<Path> paths = new HashSet<>();

		if (Files.exists(path)) {
			paths.add(path);

			if (Files.isDirectory(path)) {
				list(path, paths);
			}
		}

		return paths;
	}

	/**
	 * Will recursively list the directory or add the path without throwing any
	 * exceptions.
	 *
	 * @param path the path to add or list
	 * @param paths the set of all paths found thus far
	 */
	private static void list(Path path, Set<Path> paths) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path current : stream) {
				paths.add(current);

				if (Files.isDirectory(current)) {
					list(current, paths);
				}
			}
		}
		catch (IOException ex) {
			log.catching(Level.DEBUG, ex);
		}
	}

	/**
	 * Tests the directory listing for the current directory.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		list(Path.of(".")).stream().forEach(System.out::println);
	}
}
