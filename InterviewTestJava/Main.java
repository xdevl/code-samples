import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main
{
    /* Expected output:
    at 12:00:00, Order #1 Accepted
    at 12:00:00, Begin Cooking 4 Haddock
    at 12:00:50, Begin Cooking 3 Chips
    at 12:01:30, Begin Cooking 2 Cod
    at 12:02:50, Serve Order #1
    at 12:00:30, Order #2 Accepted
    at 12:02:50, Begin Cooking 1 Chips
    at 12:03:20, Begin Cooking 1 Haddock
    at 12:04:50, Serve Order #2
    at 12:01:00, Order #3 Rejected
     */
    protected static final String TEST1=
            "Order #1, 12:00:00, 2 Cod, 4 Haddock, 3 Chips\n"
            +"Order #2, 12:00:30, 1 Haddock, 1 Chips\n"
            +"Order #3, 12:01:00, 21 Chips" ;

    /* Expected output:
    at 12:00:00, Order #1 Accepted
    at 12:00:00, Begin Cooking 4 Chips
    at 12:02:00, Begin Cooking 4 Chips
    at 12:04:00, Serve Order #1
    at 12:00:00, Order #2 Accepted
    at 12:04:00, Begin Cooking 4 Chips
    at 12:06:00, Begin Cooking 4 Chips
    at 12:08:00, Serve Order #2
    at 12:00:00, Order #3 Rejected
    at 12:00:00, Order #4 Rejected
    at 12:02:00, Order #5 Accepted
    at 12:08:00, Begin Cooking 4 Chips
    at 12:10:00, Begin Cooking 4 Chips
    at 12:12:00, Serve Order #5
    at 12:02:00, Order #6 Rejected
    at 12:08:00, Order #7 Accepted
    at 12:12:00, Begin Cooking 4 Chips
    at 12:14:00, Begin Cooking 4 Chips
    at 12:16:00, Serve Order #7
    at 12:08:00, Order #8 Rejected
     */
    protected static final String TEST2=
            "Order #1, 12:00:00, 8 Chips\n"+
            "Order #2, 12:00:00, 8 Chips\n"+
            "Order #3, 12:00:00, 8 Chips\n"+
            "Order #4, 12:00:00, 8 Chips\n"+
            "Order #5, 12:02:00, 8 Chips\n"+
            "Order #6, 12:02:00, 8 Chips\n"+
            "Order #7, 12:08:00, 8 Chips\n"+
            "Order #8, 12:08:00, 8 Chips" ;

    protected static final String TEST3=
            "Order #1, 23:59:00, 8 Chips\n"+
            "Order #2, 23:58:00, 8 Chips\n"+
            "Order #3, 23:58:00, 8 Chips\n"+
            "Order #4, 23:59:00, 8 Chips\n"+
            "Order #5, 00:01:00, 8 Chips\n"+
            "Order #6, 00:01:00, 8 Chips\n"+
            "Order #7, 00:07:00, 8 Chips\n"+
            "Order #8, 00:07:00, 8 Chips" ;

    // The test is not clear about what time format we should use, 12H or 24H ?
    protected static final SimpleDateFormat sTimeFormat=new SimpleDateFormat("HH:mm:ss") ;
    protected static final SimpleDateFormat sTimeFormat2=new SimpleDateFormat("dd MMM YYYY HH:mm:ss") ;

    protected static class Order
    {
        public int mOrderNumber ;
        public Date mOrderTime ;
        public int mCodQuantity=0 ;
        public int mHaddockQuantity=0 ;
        public int mChipsQuantity=0 ;

        // We don't do any validation on the order and assume it is always well formed
        public Order(String command, int dayOffset) throws ParseException
        {
            String[] values=command.trim().split("\\s*,\\s*") ;
            mOrderNumber=Integer.parseInt(values[0].trim().substring("Order #".length())) ;
            mOrderTime=sTimeFormat.parse(values[1]) ;
            addDayOffset(dayOffset) ;

            int quantity=0 ;
            for(int i=2;i<values.length;++i)
                if((quantity=parseProductQuantity("Cod",values[i]))>0)
                    mCodQuantity+=quantity ;
                else if((quantity=parseProductQuantity("Haddock",values[i]))>0)
                    mHaddockQuantity+=quantity ;
                else if((quantity=parseProductQuantity("Chips",values[i]))>0)
                    mChipsQuantity+=quantity ;
        }

        private static int parseProductQuantity(String product, String toParse)
        {
            int index=toParse.indexOf(product) ;
            // We subtract one because we expect a space between the quantity and the product name
            return index>0?Integer.parseInt(toParse.substring(0,index-1)):0 ;
        }

        private void addDayOffset(int dayOffset)
        {
            mOrderTime.setTime(mOrderTime.getTime()+dayOffset*24*60*60*1000) ;
        }

        @Override
        public String toString()
        {
            return String.format("Order #%d, %s, %d Cod, %d Haddock, %d Chips",mOrderNumber,
                    sTimeFormat.format(mOrderTime),mCodQuantity,mHaddockQuantity,mChipsQuantity) ;
        }
    }

    protected static class Operation implements Comparable<Operation>
    {
        private final String mName ;
        private final long mTime ;

        public Operation(String name, long time)
        {
            mName=name ;
            mTime=time ;
        }

        public long getTime()
        {
            return mTime ;
        }

        public String getName()
        {
            return mName ;
        }

        @Override
        public int compareTo(Operation operation)
        {
            return operation==null?1:Long.compare(getTime(),operation.getTime()) ;
        }
    }

    protected static abstract class OperationQueue implements Comparable<OperationQueue>
    {
        public abstract void queue(Operation operation) ;
        public abstract void clear() ;
        public abstract void populate(Timeline timeline, long delay) ;
        public abstract long getTime() ;

        public void queue(Operation operation, int quantity)
        {
            for(int i=0;i<quantity;++i)
                queue(operation) ;
        }

        @Override
        public int compareTo(OperationQueue operationQueue)
        {
            return operationQueue==null?1:Long.compare(getTime(),operationQueue.getTime()) ;
        }
    }

    protected static class SimpleOperationQueue extends OperationQueue
    {
        private final List<Operation> mOperations=new ArrayList<>();
        private long mTime ;

        public void queue(Operation operation)
        {
            mTime+=operation.getTime() ;
            mOperations.add(operation) ;
        }

        public void clear()
        {
            mTime=0 ;
            mOperations.clear() ;
        }

        public void populate(Timeline timeline, long delay)
        {
            for(Operation operation: mOperations)
            {
                timeline.add(delay,operation) ;
                delay+=operation.getTime() ;
            }
        }

        @Override
        public long getTime()
        {
            return mTime ;
        }

        @Override
        public String toString()
        {
            String buffer="" ;
            for(Operation operation: mOperations)
                buffer+=" "+operation.getName()+" " ;
            return buffer ;
        }
    }

    protected static class CompositeOperationQueue extends OperationQueue
    {
        private final List<OperationQueue> mQueueSet=new ArrayList<>() ;
        private final boolean mHomogeneous ;

        public CompositeOperationQueue(int size)
        {
            for(int i=0;i<size;++i)
                mQueueSet.add(new SimpleOperationQueue()) ;
            mHomogeneous=true ;
        }

        public CompositeOperationQueue(OperationQueue ...operationQueues)
        {
            for(OperationQueue operationQueue: operationQueues)
                mQueueSet.add(operationQueue) ;
            mHomogeneous=false ;
        }

        public void queue(Operation operation)
        {
            if(mHomogeneous)
                Collections.min(mQueueSet).queue(operation) ;
            else throw new UnsupportedOperationException("Can't queue operation in a heterogeneous queue") ;
        }

        public void clear()
        {
            for(OperationQueue operationQueue: mQueueSet)
                operationQueue.clear() ;
        }

        public void populate(Timeline timeline, long delay)
        {
            for(OperationQueue operationQueue: mQueueSet)
                // Ok, so the following is a bit weird: queues which are different types (ie: fish and chips) should get
                // the food to end cooking the closest possible to the time it gets served but for queues which are of
                // the same type (ie: fish) we should not if we consider what the first test case is supposed to output...
                // (ie: output says: begin cooking 4 Haddocks instead of 2)
                operationQueue.populate(timeline,mHomogeneous?delay:delay+getTime()-operationQueue.getTime()) ;
        }

        @Override
        public long getTime()
        {
            return Collections.max(mQueueSet).getTime() ;
        }

        @Override
        public String toString()
        {
            String buffer="" ;
            for(OperationQueue operationQueue: mQueueSet)
                buffer+="["+operationQueue+"] -> "+operationQueue.getTime()+"\n" ;
            return buffer ;
        }
    }

    public static class Timeline
    {
        private final TreeMap<Long,TreeMap<Operation,Integer>> mTimeline=new TreeMap<>() ;
        private long mCurrentTime ;
        private long mMaxOrderCompletionTime, mMaxServedFoodTime ;

        public Timeline(Date time, long maxOrderCompletionTime, long maxServedFoodTime)
        {
            mCurrentTime=time.getTime() ;
            mMaxOrderCompletionTime=maxOrderCompletionTime*1000 ;
            mMaxServedFoodTime=maxServedFoodTime*1000 ;
        }

        public void add(long time, Operation operation)
        {
            time*=1000 ;
            TreeMap<Operation,Integer> operationMap=mTimeline.get(time) ;
            if(operationMap==null)
            {
                operationMap=new TreeMap<>() ;
                mTimeline.put(time,operationMap) ;
            }

            Integer count=operationMap.containsKey(operation)?operationMap.get(operation)+1:1 ;
            operationMap.put(operation,count) ;
        }

        public long getLastOperationEndTime()
        {
            Map.Entry<Long,TreeMap<Operation,Integer>> lastEntry=mTimeline.lastEntry() ;
            return mCurrentTime+lastEntry.getKey()+lastEntry.getValue().lastKey().getTime()*1000 ;
        }

        public long getFirstOperationEndTime()
        {
            Map.Entry<Long,TreeMap<Operation,Integer>> firstEntry=mTimeline.firstEntry() ;
            return mCurrentTime+firstEntry.getKey()+firstEntry.getValue().firstKey().getTime()*1000 ;
        }

        public boolean play(Order order)
        {
            if(!(getLastOperationEndTime()-order.mOrderTime.getTime()<=mMaxOrderCompletionTime
                    && getLastOperationEndTime()-getFirstOperationEndTime()<=mMaxServedFoodTime))
            {
                mTimeline.clear() ;
                return false ;
            }

            System.out.println("at " +sTimeFormat.format(order.mOrderTime) + ", Order #" + order.mOrderNumber + " Accepted");
            for(Map.Entry<Long,TreeMap<Operation,Integer>> instructionEntry: mTimeline.entrySet())
            {
                String buffer="at "+sTimeFormat.format(mCurrentTime+instructionEntry.getKey())+", Begin Cooking" ;
                // The test doesn't give any hint on how instructions with multiple food to
                // cook at the same given time should look like :(
                for(Map.Entry<Operation,Integer> operationEntry: instructionEntry.getValue().entrySet())
                    buffer+=" "+operationEntry.getValue()+" "+operationEntry.getKey().getName()+"," ;
                System.out.println(buffer.substring(0,buffer.length()-1)) ;
            }

            mCurrentTime=getLastOperationEndTime() ;
            mTimeline.clear() ;
            System.out.println("at " + sTimeFormat.format(mCurrentTime) + ", Serve Order #" + order.mOrderNumber);
            return true ;
        }
    }

    public static void main(String args[] ) throws Exception
    {
        List<Order> orders=new ArrayList<>() ;
        Scanner scanner=new Scanner(TEST1) ;
        Order lastOrder=null ;
        int dayCount=0 ;
        while(scanner.hasNextLine())
        {
            Order order=new Order(scanner.nextLine(),dayCount) ;
            orders.add(order) ;
            // We make the assumption orders are entered sequentially so if an order is anterior
            // to the last entered order, we assume it has been done the next day
            if(lastOrder!=null && order.mOrderTime.before(lastOrder.mOrderTime))
                order.addDayOffset(1) ;
            lastOrder=order ;
        }

        Operation cookCod=new Operation("Cod",80), cookHaddock=new Operation("Haddock",90), cookChips=new Operation("Chips",120) ;
        OperationQueue chipFryer=new CompositeOperationQueue(4), fishFryer=new CompositeOperationQueue(4),
                kitchen=new CompositeOperationQueue(chipFryer,fishFryer) ;
        Timeline timeline =null ;


        for(Order order: orders)
        {
            if(timeline==null)
                timeline=new Timeline(order.mOrderTime,600,120) ;

            // The queueing algorithm is really simple: queue the longest food to cook first
            chipFryer.queue(cookChips,order.mChipsQuantity) ;
            fishFryer.queue(cookHaddock,order.mHaddockQuantity) ;
            fishFryer.queue(cookCod,order.mCodQuantity) ;

            // Generate a set of instructions depending on how we queued the food to cook
            kitchen.populate(timeline,0) ;

            if(!timeline.play(order))
                // The order doesn't meet the timing criteria...
                System.out.println("at "+sTimeFormat.format(order.mOrderTime)+", Order #"+order.mOrderNumber+" Rejected") ;

            // Clear the queues in order to process the next order
            kitchen.clear() ;
        }
    }
}
