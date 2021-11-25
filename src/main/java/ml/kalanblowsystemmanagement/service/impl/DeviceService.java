
package ml.kalanblowsystemmanagement.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowsystemmanagement.model.Device;
import ml.kalanblowsystemmanagement.model.User;
import ml.kalanblowsystemmanagement.repository.DeviceRepository;

import static java.util.Objects.nonNull;
import ua_parser.Client;
import ua_parser.Parser;

@Component

@Slf4j
public class DeviceService {
	private static final String UNKNOWN = "UNKNOWN";

	private DeviceRepository deviceRepository;

	private DatabaseReader databaseReader;

	private Parser parser;

	private MessageSource messages;

	public void verifyDevice(User user, HttpServletRequest request) throws IOException, GeoIp2Exception {

		String ip = extractIp(request);
		String location = getIpLocation(ip);

		String deviceDetails = getDeviceDetails(request.getHeader("user-agent"));

		Device existingDevice = findExistingDevice(user.getId(), deviceDetails, location);

		if (Objects.isNull(existingDevice)) {

			Device Device = new Device();
			Device.setUserId(user.getId());
			Device.setLocation(location);
			Device.setDeviceDetails(deviceDetails);
			Device.setLastLoggedIn(LocalDateTime.now());
			deviceRepository.save(Device);
		} else {
			existingDevice.setLastLoggedIn(LocalDateTime.now());
			deviceRepository.save(existingDevice);
		}

	}

	private String extractIp(HttpServletRequest request) {
		String clientIp;
		String clientXForwardedForIp = request.getHeader("x-forwarded-for");
		if (nonNull(clientXForwardedForIp)) {
			clientIp = parseXForwardedHeader(clientXForwardedForIp);
		} else {
			clientIp = request.getRemoteAddr();
		}

		return clientIp;
	}

	private String parseXForwardedHeader(String header) {
		return header.split(" *, *")[0];
	}

	private String getDeviceDetails(String userAgent) {
		String deviceDetails = UNKNOWN;

		Client client = parser.parse(userAgent);
		if (Objects.nonNull(client)) {
			deviceDetails = client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor
					+ " - " + client.os.family + " " + client.os.major + "." + client.os.minor;
		}

		return deviceDetails;
	}

	private String getIpLocation(String ip) throws IOException, GeoIp2Exception {

		String location = UNKNOWN;

		InetAddress ipAddress = InetAddress.getByName(ip);

		CityResponse cityResponse = databaseReader.city(ipAddress);
		if (Objects.nonNull(cityResponse) && Objects.nonNull(cityResponse.getCity())
				&& !Strings.isNullOrEmpty(cityResponse.getCity().getName())) {

			location = cityResponse.getCity().getName();
		}

		return location;
	}

	private Device findExistingDevice(Long userId, String deviceDetails, String location) {

		List<Device> knownDevices = deviceRepository.findByUserId(userId);

		for (Device existingDevice : knownDevices) {
			if (existingDevice.getDeviceDetails().equals(deviceDetails)
					&& existingDevice.getLocation().equals(location)) {
				return existingDevice;
			}
		}

		return null;
	}

	
}
