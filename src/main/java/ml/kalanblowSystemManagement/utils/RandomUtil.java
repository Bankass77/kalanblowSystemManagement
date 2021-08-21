package ml.kalanblowSystemManagement.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomUtil {
	 private static final ThreadLocal<Random> THREAD_LOCAL_RANDOM;

	  static {
	    THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(RandomUtil::createSecureRandom);
	  }

	  public static IntStream ints(long streamSize) {
	    return THREAD_LOCAL_RANDOM.get().ints(streamSize);
	  }

	  public static void nextBytes(byte[] bytes) {
	    THREAD_LOCAL_RANDOM.get().nextBytes(bytes);
	  }

	  public static int nextInt() {
	    return THREAD_LOCAL_RANDOM.get().nextInt();
	  }

	  private static Random createSecureRandom() {
	    try {
	      return SecureRandom.getInstanceStrong();
	    } catch (NoSuchAlgorithmException nae) {
	      log.warn("Couldn't create strong secure random generator; reason: {}.", nae.getMessage());
	      return new SecureRandom();
	    }
	  }


}
