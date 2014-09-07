
public class MythicPlayer {
	/*
	 * This class provides a mapping wrapper for EntityPlayer.
	 * It will have to be updated as the obfuscation changes (or becomes unneccessary).
	 */
	
	//set the type of this field to the (obfuscated or not) PlayerEntity type.
	//XXX: obfuscation reference
	ahd entityPlayer = null;
	
	public MythicPlayer(ahd vanillaEntityPlayer){
		entityPlayer = vanillaEntityPlayer;
	}
	
	public void sendChatMessage(String message){
		//should point at EntityPlayer.addChatComponentMessage(IChatComponent p_146105_1_)
		//XXX: obfuscation reference
		hu chatComponentMessage = new hu(message);
		//XXX: obfuscation reference
		entityPlayer.a(chatComponentMessage);
	}
	
	public void travelToDimension(int dimId){
		//XXX: obfuscation reference
		((wv)entityPlayer).c(dimId);
	}
	
	public void setPosition(double x, double y, double z){
		//XXX: obfuscation reference
		((wv) entityPlayer).a(x, y, z);
	}
	
	public void teleportToPlayer(MythicPlayer target){
		if(getDimension() != target.getDimension()){
			travelToDimension(target.getDimension());
		}
		setPosition(target.getX(), target.getY(), target.getZ());
	}
	
	public double getX(){
		//XXX: obfuscation reference
		return ((wv)entityPlayer).s;
	}
	
	public double getY(){
		//XXX: obfuscation reference
		return ((wv)entityPlayer).t;
	}
	
	public double getZ(){
		//XXX: obfuscation reference
		return ((wv)entityPlayer).u;
	}
	
	public int getDimension(){
		//XXX: obfuscation reference
		return ((wv)entityPlayer).am;
	}
	
	public boolean isUsingItem(){
		//XXX: obfuscation reference
		return entityPlayer.bR();
	}
	
	public void stopUsingItem(){
		//XXX: obfuscation reference
		entityPlayer.bT();
	}
	
	public void clearItemInUse(){
		//XXX: obfuscation reference
		entityPlayer.bU();
	}

	public String getName(){
		//XXX: obfuscation reference
		return entityPlayer.d_();
	}
	
	public String getUUIDString(){
		//XXX: obfuscation reference
		return entityPlayer.ao.toString();
	}
	
	public void heal(float amount){
		//XXX: obfuscation reference
		((xm)entityPlayer).g(amount);
	}
	
	public void setHealth(float amount){
		//XXX: obfuscation reference
		((xm)entityPlayer).h(amount);
	}
	public MythicItem getEquipmentInSlot(int slot){
		//XXX: obfuscation reference
		return new MythicItem(entityPlayer.p(slot));
	}
	public MythicItem getHeldItem(){
		//XXX: obfuscation reference
		return new MythicItem(entityPlayer.bz());
	}
	public void setCurrentItemOrArmor(int slot, MythicItem item){
		//XXX: obfuscation reference
		entityPlayer.c(slot, item.itemStack);
	}
	//void onDeath(DamageSource var1) a(wh var1)
	//void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)
}
