package it.neokree.materialtabs;

public interface MaterialTabListener {
	public void onTabSelected(MaterialTab tab);
	
	public void onTabReselected(MaterialTab tab);
	
	public void onTabUnselected(MaterialTab tab);
}
