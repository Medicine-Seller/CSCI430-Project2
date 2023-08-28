import java.util.*;

public class Wishlist {
	List<Entry> entries = new LinkedList<Entry>();
	
	private int getIndexIfEntryExist(Entry e) {
		for (int i = 0; i < entries.size(); i++)
			if (entries.get(i).getProduct() == e.getProduct())
				return i;
			
		return -1;
	}
	
	public Iterator<Entry> getEntries(){
		return entries.iterator();
	}
	
	public void addEntry(Entry e) {
		int index = getIndexIfEntryExist(e);
		
		if (index == -1){
			entries.add(e);
			return;
		}
		
		entries.get(index).add(e.getQuantity());
	}
	
	public void subEntry(Entry e) {
		int index = getIndexIfEntryExist(e);
		
		if (index == -1){
			return;
		}
		
		entries.get(index).sub(e.getQuantity());
		if (entries.get(index).getQuantity() <= 0)
			entries.remove(index);
	}
	
	public boolean removeEntry(int entryId) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getProduct().getId() == entryId) {
				entries.remove(i);
				return true;
			}	
		}
		return false;
	}

}
