package model;


import java.awt.Color;
import java.util.HashMap;

public class ColorObj 
{
	static HashMap <String,Color> colorMap = new HashMap<String,Color>();
	
	private static void init()
	{
		colorMap.put("black",new Color(0,0,0));
		colorMap.put("navy",new Color(0,0,128));
		colorMap.put("darkblue",new Color(0,0,139));
		colorMap.put("mediumblue",new Color(0,0,205));
		colorMap.put("blue",new Color(0,0,255));
		colorMap.put("darkgreen",new Color(0,100,0));
		colorMap.put("green",new Color(0,128,0));
		colorMap.put("teal",new Color(0,128,128));
		colorMap.put("darkcyan",new Color(0,139,139));
		colorMap.put("deepskyblue",new Color(0,191,255));
		colorMap.put("darkturquoise",new Color(0,206,209));
		colorMap.put("mediumspringgreen",new Color(0,250,154));
		colorMap.put("lime",new Color(0,255,0));
		colorMap.put("springgreen",new Color(0,255,127));
		colorMap.put("aqua",new Color(0,255,255));
		colorMap.put("midnightblue",new Color(25,25,112));
		colorMap.put("dodgerblue",new Color(30,144,255));
		colorMap.put("lightseagreen",new Color(32,178,170));
		colorMap.put("forestgreen",new Color(34,139,34));
		colorMap.put("seagreen",new Color(46,139,87));
		colorMap.put("darkslategray",new Color(47,79,79));
		colorMap.put("limegreen",new Color(50,205,50));
		colorMap.put("mediumseagreen",new Color(60,179,113));
		colorMap.put("turquoise",new Color(64,224,208));
		colorMap.put("royalblue",new Color(65,105,225));
		colorMap.put("steelblue",new Color(70,130,180));
		colorMap.put("darkslateblue",new Color(72,61,139));
		colorMap.put("mediumturquoise",new Color(72,209,204));
		colorMap.put("indigo",new Color(75,0,130));
		colorMap.put("darkolivegreen",new Color(85,107,47));
		colorMap.put("cadetblue",new Color(95,158,160));
		colorMap.put("cornflowerblue",new Color(100,149,237));
		colorMap.put("mediumaquamarine",new Color(102,205,170));
		colorMap.put("dimgray",new Color(105,105,105));
		colorMap.put("slateblue",new Color(106,90,205));
		colorMap.put("olivedrab",new Color(107,142,35));
		colorMap.put("slategray",new Color(112,128,144));
		colorMap.put("lightslategray",new Color(119,136,153));
		colorMap.put("mediumslateblue",new Color(123,104,238));
		colorMap.put("lawngreen",new Color(124,252,0));
		colorMap.put("chartreuse",new Color(127,255,0));
		colorMap.put("aquamarine",new Color(127,255,212));
		colorMap.put("maroon",new Color(128,0,0));
		colorMap.put("purple",new Color(128,0,128));
		colorMap.put("olive",new Color(128,128,0));
		colorMap.put("gray",new Color(128,128,128));
		colorMap.put("skyblue",new Color(135,206,235));
		colorMap.put("lightskyblue",new Color(135,206,250));
		colorMap.put("blueviolet",new Color(138,43,226));
		colorMap.put("darkred",new Color(139,0,0));
		colorMap.put("darkmagenta",new Color(139,0,139));
		colorMap.put("saddlebrown",new Color(139,69,19));
		colorMap.put("darkseagreen",new Color(143,188,143));
		colorMap.put("lightgreen",new Color(144,238,144));
		colorMap.put("mediumpurple",new Color(147,112,219));
		colorMap.put("darkviolet",new Color(148,0,211));
		colorMap.put("palegreen",new Color(152,251,152));
		colorMap.put("darkorchid",new Color(153,50,204));
		colorMap.put("yellowgreen",new Color(154,205,50));
		colorMap.put("sienna",new Color(160,82,45));
		colorMap.put("brown",new Color(165,42,42));
		colorMap.put("darkgray",new Color(169,169,169));
		colorMap.put("lightblue",new Color(173,216,230));
		colorMap.put("greenyellow",new Color(173,255,47));
		colorMap.put("paleturquoise",new Color(175,238,238));
		colorMap.put("lightsteelblue",new Color(176,196,222));
		colorMap.put("powderblue",new Color(176,224,230));
		colorMap.put("firebrick",new Color(178,34,34));
		colorMap.put("darkgoldenrod",new Color(184,134,11));
		colorMap.put("mediumorchid",new Color(186,85,211));
		colorMap.put("rosybrown",new Color(188,143,143));
		colorMap.put("darkkhaki",new Color(189,183,107));
		colorMap.put("silver",new Color(192,192,192));
		colorMap.put("mediumvioletred",new Color(199,21,133));
		colorMap.put("indianred",new Color(205,92,92));
		colorMap.put("chocolate",new Color(210,105,30));
		colorMap.put("lightgrey",new Color(211,211,211));
		colorMap.put("thistle",new Color(216,191,216));
		colorMap.put("orchid",new Color(218,112,214));
		colorMap.put("goldenrod",new Color(218,165,32));
		colorMap.put("palevioletred",new Color(219,112,147));
		colorMap.put("crimson",new Color(220,20,60));
		colorMap.put("gainsboro",new Color(220,220,220));
		colorMap.put("burlywood",new Color(222,184,135));
		colorMap.put("lightcyan",new Color(224,255,255));
		colorMap.put("lavender",new Color(230,230,250));
		colorMap.put("darksalmon",new Color(233,150,122));
		colorMap.put("violet",new Color(238,130,238));
		colorMap.put("palegoldenrod",new Color(238,232,170));
		colorMap.put("lightcoral",new Color(240,128,128));
		colorMap.put("khaki",new Color(240,230,140));
		colorMap.put("aliceblue",new Color(240,248,255));
		colorMap.put("honeydew",new Color(240,255,240));
		colorMap.put("azure",new Color(240,255,255));
		colorMap.put("sandybrown",new Color(244,164,96));
		colorMap.put("wheat",new Color(245,222,179));
		colorMap.put("beige",new Color(245,245,220));
		colorMap.put("whitesmoke",new Color(245,245,245));
		colorMap.put("mintcream",new Color(245,255,250));
		colorMap.put("ghostwhite",new Color(248,248,255));
		colorMap.put("salmon",new Color(250,128,114));
		colorMap.put("antiquewhite",new Color(250,235,215));
		colorMap.put("linen",new Color(250,240,230));
		colorMap.put("lightgoldenrodyellow",new Color(250,250,210));
		colorMap.put("oldlace",new Color(253,245,230));
		colorMap.put("red",new Color(255,0,0));
		colorMap.put("magenta",new Color(255,0,255));
		colorMap.put("deeppink",new Color(255,20,147));
		colorMap.put("orangered",new Color(255,69,0));
		colorMap.put("tomato",new Color(255,99,71));
		colorMap.put("hotpink",new Color(255,105,180));
		colorMap.put("coral",new Color(255,127,80));
		colorMap.put("darkorange",new Color(255,140,0));
		colorMap.put("lightsalmon",new Color(255,160,122));
		colorMap.put("orange",new Color(255,165,0));
		colorMap.put("lightpink",new Color(255,182,193));
		colorMap.put("peachpuff",new Color(255,218,185));
		colorMap.put("navajowhite",new Color(255,222,173));
		colorMap.put("moccasin",new Color(255,228,181));
		colorMap.put("bisque",new Color(255,228,196));
		colorMap.put("mistyrose",new Color(255,228,225));
		colorMap.put("blanchedalmond",new Color(255,235,205));
		colorMap.put("papayawhip",new Color(255,239,213));
		colorMap.put("lavenderblush",new Color(255,240,245));
		colorMap.put("seashell",new Color(255,245,238));
		colorMap.put("cornsilk",new Color(255,248,220));
		colorMap.put("lemonchiffon",new Color(255,250,205));
		colorMap.put("floralwhite",new Color(255,250,240));
		colorMap.put("yellow",new Color(255,255,0));
		colorMap.put("lightyellow",new Color(255,255,224));
		colorMap.put("ivory",new Color(255,255,240));
		colorMap.put("white",new Color(255,255,255));
		colorMap.put("none",new Color(0,0,0,0));
	}
	
	/**
	 * return RGB color given color name
	 * 
	 * @param name
	 * @return color with the specified name
	 */
	public static Color getRGBColor(String name)
	{
		init();
		
		Color color = new Color(0,0,0,0);
		
		if(colorMap.containsKey(name))
		{
			return colorMap.get(name);
		}
		else
		{	
			try
			{
				color = Color.decode(name);
			}
			catch(Exception e)
			{
				color = new Color(0,0,0,0);
			}
		}
		
		return color;
	}
}
