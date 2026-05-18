package game.gui.view;

import java.io.File;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Monsters Inc.–style board emblem from the Monster Board Figma file.
 */
public final class MonsterBoardLogo {

	private MonsterBoardLogo() {
	}

	public static StackPane create(double size) {
		File export = new File("resources/board-logo.png");
		if (export.isFile()) {
			ImageView image = new ImageView(new Image(export.toURI().toString(), true));
			image.setPreserveRatio(true);
			image.setFitWidth(size);
			image.setFitHeight(size);
			image.setMouseTransparent(true);
			StackPane pane = new StackPane(image);
			pane.setMouseTransparent(true);
			return pane;
		}
		return new StackPane(buildVectorLogo(size));
	}

	private static Group buildVectorLogo(double size) {
		double r = size * 0.48;

		Circle ring = new Circle(r);
		ring.setFill(Color.TRANSPARENT);
		ring.setStroke(Color.web(BoardTheme.LOGO_BLUE));
		ring.setStrokeWidth(size * 0.045);

		Text letterM = new Text("M");
		letterM.setFont(Font.font("Arial Black", FontWeight.BOLD, size * 0.52));
		letterM.setFill(Color.web(BoardTheme.LOGO_BLUE));
		letterM.setTranslateX(-letterM.getLayoutBounds().getWidth() / 2);
		letterM.setTranslateY(letterM.getLayoutBounds().getHeight() / 3.2);

		Ellipse eyeWhite = new Ellipse(size * 0.11, size * 0.055);
		eyeWhite.setFill(Color.WHITE);
		eyeWhite.setTranslateY(size * 0.02);

		Circle pupil = new Circle(size * 0.038);
		pupil.setFill(Color.BLACK);
		pupil.setTranslateY(size * 0.02);

		Group group = new Group(ring, letterM, eyeWhite, pupil);
		group.setMouseTransparent(true);
		return group;
	}
}
