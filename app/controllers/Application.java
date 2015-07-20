package controllers;

import java.io.File;
import java.io.FilenameFilter;

import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public enum Error {
		
    	E404A("404", "Application does not exist."),
		E404B("404", "Folder does not exist for given application."),
		E404C("404", "Image not available.");
		
		Error(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String code;
		public String message;

	}
    
    public static class ErrorResponse {
		public String code;
		public String message;

		public ErrorResponse(String code, String message) {
			this.code = code;
			this.message = message;
		}
	}
    
    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp", "jpg", "jpeg" // and other formats you need
    };
    
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    
    public static Result getAd(){
    	String appName = request().getQueryString("appName");
    	String folderName = request().getQueryString("folderName");
    	String appPath = Play.application().configuration().getString("path")+"/"+appName;
        File app = new File(appPath);
        if (!app.isDirectory()) {
        	return ok(Json.toJson(new ErrorResponse(Error.E404A.code,Error.E404A.message)));
        }
        String folderPath = appPath+"/"+folderName;
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
        	return ok(Json.toJson(new ErrorResponse(Error.E404B.code,Error.E404B.message)));
        }
        File[] images = folder.listFiles(IMAGE_FILTER);
        if(images.length == 0){
        	return ok(Json.toJson(new ErrorResponse(Error.E404C.code,Error.E404C.message)));
        }
        File image = images[0];
        /*BufferedImage img = null;
        try {
            img = ImageIO.read(image);
        } catch (final IOException e) {
            e.printStackTrace();
        }*/
    	return ok(image);
    }
  
}
