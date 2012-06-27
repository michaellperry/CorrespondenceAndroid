package com.updatecontrols.correspondence.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.updatecontrols.correspondence.memento.CorrespondenceFactType;
import com.updatecontrols.correspondence.memento.FactMemento;
import com.updatecontrols.correspondence.memento.FactID;
import com.updatecontrols.correspondence.memento.RoleMemento;

public class MementoTests {

	private static final CorrespondenceFactType MyType = new CorrespondenceFactType("com.updatecontrols.correspondence.test.MyType", 1);
	private static final RoleMemento MyTypeNext = new RoleMemento(MyType, "next", false);
	private static final RoleMemento MyTypeFirst = new RoleMemento(MyType, "first", false);
	private static final RoleMemento MyTypeSecond = new RoleMemento(MyType, "second", false);

	@Test
	public void testNotEqualType() throws Exception {
		FactMemento one = new FactMemento(MyType);
		FactMemento two = new FactMemento(new CorrespondenceFactType("com.updatecontrols.correspondence.test.MyType", 2));
		assertFalse(one.equals(two));
		assertFalse(one.hashCode() == two.hashCode());
	}

	@Test
	public void testEqualType() throws Exception {
		FactMemento one = new FactMemento(MyType);
		FactMemento two = new FactMemento(MyType);
		assertTrue(one.equals(two));
		assertTrue(one.hashCode() == two.hashCode());
	}

	@Test
	public void testNotEqualData() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112, 6 });
		assertFalse(one.equals(two));
		assertFalse(one.hashCode() == two.hashCode());
	}

	@Test
	public void testEqualData() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112 });
		assertTrue(one.equals(two));
		assertTrue(one.hashCode() == two.hashCode());
	}

	@Test
	public void testNotEqualPredecessors() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		one.addPredecessor(MyTypeNext, new FactID(34L));
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112 });
		two.addPredecessor(MyTypeNext, new FactID(35L));
		assertFalse(one.equals(two));
		assertFalse(one.hashCode() == two.hashCode());
	}

	@Test
	public void testEqualPredecessors() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		one.addPredecessor(MyTypeNext, new FactID(34L));
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112 });
		two.addPredecessor(MyTypeNext, new FactID(34L));
		assertTrue(one.equals(two));
		assertTrue(one.hashCode() == two.hashCode());
	}

	@Test
	public void testEqualPredecessorsDifferentOrder() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		one.addPredecessor(MyTypeFirst, new FactID(134L));
		one.addPredecessor(MyTypeSecond, new FactID(234L));
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112 });
		two.addPredecessor(MyTypeSecond, new FactID(234L));
		two.addPredecessor(MyTypeFirst, new FactID(134L));
		assertTrue(one.equals(two));
		assertTrue(one.hashCode() == two.hashCode());
	}

	@Test
	public void testNotEqualPredecessorsDifferentMapping() throws Exception {
		FactMemento one = new FactMemento(MyType);
		one.setData(new byte[] { 2, 4, 127, -112 });
		one.addPredecessor(MyTypeFirst, new FactID(134L));
		one.addPredecessor(MyTypeSecond, new FactID(234L));
		FactMemento two = new FactMemento(MyType);
		two.setData(new byte[] { 2, 4, 127, -112 });
		two.addPredecessor(MyTypeSecond, new FactID(134L));
		two.addPredecessor(MyTypeFirst, new FactID(234L));
		assertFalse(one.equals(two));
		assertFalse(one.hashCode() == two.hashCode());
	}
	
	@Test
	public void testOrderIndependent() throws Exception {
		FactMemento player1 = new FactMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1));
		player1.setData(new byte[] { 0,0,0,0 });
		player1.addPredecessor(new RoleMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1), "game", false), new FactID(1027));
		player1.addPredecessor(new RoleMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1), "user", false), new FactID(1014));
		int hashCode1 = player1.hashCode();

		FactMemento player2 = new FactMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1));
		player2.setData(new byte[] { 0,0,0,0 });
		player2.addPredecessor(new RoleMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1), "user", true), new FactID(1014));
		player2.addPredecessor(new RoleMemento(new CorrespondenceFactType("FacetedWorlds.Reversi.Model.Player", 1), "game", true), new FactID(1027));
		int hashCode2 = player2.hashCode();
		
		assertEquals(hashCode1, hashCode2);
		assertEquals(-1337793359, hashCode1);
		assertEquals(-1337793359, hashCode2);
		
		assertEquals(player1, player2);
	}
}
