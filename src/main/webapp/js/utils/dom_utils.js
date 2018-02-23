/**
 * Misc javascript utilities
 * 
 * @author Kien Huynh
 */


function respond() {
	/**
	 * Response to the window size changes
	 */
	x = document.getElementById("khMenu");
	if (x.className == "khmenu") {
		x.className = "khmenu responsive";
	} else {
		x.className = "khmenu";
	}
}