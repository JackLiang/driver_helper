/**
 *  
 */
package com.dh.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * 
 * @author ying_liu 2015年4月13日
 */
public class ImageUtils {
	private static Logger LOG = LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * 几种常见的图片格式
	 */
	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public final static void scale(String srcImageFile, String result, int scale, boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			LOG.error("缩放图像（按比例缩放）失败。", e);
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param bb
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 */
	public final static BufferedImage scale2(String srcImageFile, int height, int width, boolean bb) {
		Image itemp = null;
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
		} catch (IOException e) {
			LOG.error("缩放图像（按高度和宽度缩放）失败。", e);
		}
		return (BufferedImage) itemp;
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param bb
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 */
	public final static BufferedImage scale2(BufferedImage srcImage, int height, int width, boolean bb) {
		Image itemp = null;
		try {
			double ratio = 0.0; // 缩放比例
			itemp = srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			// 计算比例
			if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
				if (srcImage.getHeight() > srcImage.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(srcImage, null);
			}
			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
		} catch (Exception e) {
			LOG.error("缩放图像（按高度和宽度缩放）失败。", e);
		}
		return (BufferedImage) itemp;
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param destImageFile
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param bb
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 */
	public final static void scale2(String srcImageFile, String destImageFile, int height, int width, boolean bb) {
		try {
			BufferedImage image = scale2(srcImageFile, height, width, bb);
			ImageIO.write(image, "JPEG", new File(destImageFile));
		} catch (IOException e) {
			LOG.error("缩放图像（按高度和宽度缩放）失败。", e);
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(result));
			}
		} catch (Exception e) {
			LOG.error("图像切割(按指定起点坐标和宽高切割)失败。", e);
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param rows
	 *            目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols
	 *            目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public final static void cut2(String srcImageFile, String descDir, int rows, int cols) {
		try {
			if (rows <= 0 || rows > 20)
				rows = 2; // 切片行数
			if (cols <= 0 || cols > 20)
				cols = 2; // 切片列数
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % cols == 0) {
					destWidth = srcWidth / cols;
				} else {
					destWidth = (int) Math.floor(srcWidth / cols) + 1;
				}
				if (srcHeight % rows == 0) {
					destHeight = srcHeight / rows;
				} else {
					destHeight = (int) Math.floor(srcWidth / rows) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			LOG.error("图像切割（指定切片的行数和列数）。", e);
		}
	}

	/**
	 * 图像切割（指定切片的宽度和高度）
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度。默认200
	 * @param destHeight
	 *            目标切片高度。默认150
	 */
	public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
		try {
			if (destWidth <= 0)
				destWidth = 200; // 切片宽度
			if (destHeight <= 0)
				destHeight = 150; // 切片高度
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			LOG.error("图像切割（指定切片的宽度和高度）失败。", e);
		}
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param formatName
	 *            包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static void convert(String srcImageFile, String formatName, String destImageFile) {
		try {
			File f = new File(srcImageFile);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, formatName, new File(destImageFile));
		} catch (Exception e) {
			LOG.error("图像类型转换失败。", e);
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static void gray(String srcImageFile, String destImageFile) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(destImageFile));
		} catch (IOException e) {
			LOG.error("彩色转为黑白失败。", e);
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param pressText
	 *            水印文字
	 * @param srcImageFile
	 *            源图像地址
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式
	 * @param color
	 *            水印的字体颜色
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static BufferedImage pressText(String pressText, String srcImageFile, String fontName, int fontStyle, Color color, int fontSize,
			int x, int y, float alpha) {
		BufferedImage image = null;
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			// TODO 去掉下面两行代码注释，则会出现文字水印没写入的问题，待后续研究
			// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
			// alpha));
			// // 在指定坐标绘制水印文字
			// g.drawString(pressText, (width - (getLength(pressText) *
			// fontSize))
			// / 2 + x, (height - fontSize) / 2 + y);
			g.drawString(pressText, width - x, height - y);
			g.dispose();
		} catch (Exception e) {
			LOG.error("给图片添加文字水印失败。", e);
		}
		return image;
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param pressText
	 *            水印文字
	 * @param src
	 *            源图像
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式
	 * @param color
	 *            水印的字体颜色
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static BufferedImage pressText(String pressText, BufferedImage src, String fontName, int fontStyle, Color color, int fontSize,
			int x, int y, float alpha) {
		BufferedImage image = null;
		try {
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.drawString(pressText, x, y);
			g.dispose();
		} catch (Exception e) {
			LOG.error("给图片添加文字水印失败。", e);
		}
		return image;
	}

	/**
	 * 给图片添加文字水印并输出到目标文件
	 * 
	 * @param pressText
	 *            水印文字
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式
	 * @param color
	 *            水印的字体颜色
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void savePressTextImg(String pressText, String srcImageFile, String destImageFile, String fontName, int fontStyle,
			Color color, int fontSize, int x, int y, float alpha) {
		try {
			BufferedImage image = pressText(pressText, srcImageFile, fontName, fontStyle, color, fontSize, x, y, alpha);

			// 获取保存的图片扩展名
			String imgType = getExtensionName(destImageFile);
			ImageIO.write(image, imgType, new File(destImageFile));// 输出到文件流
		} catch (Exception e) {
			LOG.error("给图片添加文字水印失败。", e);
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param pressImg
	 *            水印图片
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param x
	 *            修正值。 默认在中间
	 * @param y
	 *            修正值。 默认在中间
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(String pressImg, String srcImageFile, String destImageFile, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			Image src_biao = ImageIO.read(new File(pressImg));
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height -
			// height_biao) / 2, wideth_biao, height_biao, null);
			/*水印按图片大小进行比例适配*/
			int mark_w = (int) (wideth * 0.25);
			int mark_h = height_biao * mark_w / wideth_biao;
			g.drawImage(src_biao, (wideth - mark_w), (height - mark_h), mark_w, mark_h, null);// 放在右下角
			// 水印文件结束
			g.dispose();

			// 获取保存的图片扩展名
			String imgType = getExtensionName(destImageFile);
			ImageIO.write((BufferedImage) image, imgType, new File(destImageFile));
		} catch (Exception e) {
			LOG.error("给图片添加图片水印失败。", e);
		}
	}

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * 
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 *            文件名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * 合并图片，以toImagePath图片为模板，将fromImagePath图片合并过去
	 * 
	 * @param fromImagePath
	 *            待合并图片
	 * @param toImagePath
	 *            模板图片
	 * @param x
	 *            待合并图片的横坐标修正值（相对于模板图片）
	 * @param y
	 *            待合并图片的纵坐标修正值（相对于模板图片）
	 * @return
	 */
	public static BufferedImage mergeImage(String fromImagePath, String toImagePath, int x, int y) {
		BufferedImage fromImage = null;
		BufferedImage toImage = null;
		try {
			fromImage = ImageIO.read(new File(fromImagePath));
			toImage = ImageIO.read(new File(toImagePath));

			int w = fromImage.getWidth();
			int h = fromImage.getHeight();

			Graphics2D g = toImage.createGraphics();
			g.drawImage(fromImage, x, y, w, h, null);
			g.dispose();
		} catch (Exception e) {
			LOG.error("合并图片失败。fromImagePath=" + fromImagePath + ",toImagePath=" + toImagePath, e);
		}

		return toImage;
	}

	/**
	 * 合并图片，以toImagePath图片为模板，将fromImagePath图片合并过去
	 * 
	 * @param fromImage
	 *            待合并图片
	 * @param toImage
	 *            模板图片
	 * @param x
	 *            待合并图片的横坐标修正值（相对于模板图片）
	 * @param y
	 *            待合并图片的纵坐标修正值（相对于模板图片）
	 * @return
	 */
	public static BufferedImage mergeImage(BufferedImage fromImage, BufferedImage toImage, int x, int y) {
		try {
			int w = fromImage.getWidth();
			int h = fromImage.getHeight();

			Graphics2D g = toImage.createGraphics();
			g.drawImage(fromImage, x, y, w, h, null);
			g.dispose();
		} catch (Exception e) {
			LOG.error("合并图片失败。", e);
		}

		return toImage;
	}

	/**
	 * 合并图片，以toImagePath图片为模板，将fromImagePath图片合并过去,并将结果输出到目标文件
	 * 
	 * @param fromImagePath
	 *            待合并图片
	 * @param toImagePath
	 *            模板图片
	 * @param x
	 *            待合并图片的横坐标修正值（相对于模板图片）
	 * @param y
	 *            待合并图片的纵坐标修正值（相对于模板图片）
	 * @param destImageFile
	 *            目标输出图片文件
	 * @return
	 */
	public static void saveMergeImage(String fromImagePath, String toImagePath, int x, int y, String destImageFile) {
		try {
			BufferedImage image = mergeImage(fromImagePath, toImagePath, x, y);
			// 获取保存的图片扩展名
			String imgType = getExtensionName(destImageFile);
			ImageIO.write(image, imgType, new File(destImageFile));// 输出到文件流
		} catch (Exception e) {
			LOG.error("合并图片失败。fromImagePath=" + fromImagePath + ",toImagePath=" + toImagePath, e);
		}
	}

	/**
	 * 合并图片，以toImagePath图片为模板，将fromImagePath图片合并过去,并将结果输出到目标文件
	 * 
	 * @param image
	 *            图片
	 * @param destImageFile
	 *            目标输出图片文件
	 * @return
	 */
	public static void saveMergeImage(BufferedImage image, String destImageFile) {
		try {
			// 获取保存的图片扩展名
			String imgType = getExtensionName(destImageFile);
			ImageIO.write(image, imgType, new File(destImageFile));// 输出到文件流
		} catch (Exception e) {
			LOG.error("合并图片失败。", e);
		}
	}

}