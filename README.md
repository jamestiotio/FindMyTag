# FindMyTag
SUTD 50.003 ESC Indoor Tracking Project with [UnaBiz](https://www.unabiz.com/)

Cohort 3 Group 7 Team Members:

- [Darren Loh Zhao Ying](https://github.com/Darren-Loh)
- [Harshit Garg](https://github.com/harshitgarg03)
- [Seah Zen Yi](https://github.com/zenibbles)
- [Dong Jiajie](https://github.com/djj067)
- [James Raphael Tiovalen](https://github.com/jamestiotio)



## Future TODOs

- Improve current algorithms with more collected dataset points.
- Add more different types/kinds of algorithms.
- Add building ID and room ID identifiers, as well as the relative position to those rooms (inside or outside).
- Add user orientation and angle detection features (use accelerometer, gyroscope and magnetometer?).
- On top of classification features (actual building and floor identification, as well as distance and location coordinates estimation), add regression features (actual longitude and latitude estimation) without using GPS (since GPS tends to be less accurate for indoor context/settings/environments).
- Add choice of using BLE Bluetooth beacons for multilateration algorithm (some useful tools might include the [log-distance path loss model](https://wikipedia.org/wiki/Log-distance_path_loss_model) or the [Levenberg-Marquardt algorithm](https://wikipedia.org/wiki/Levenberg–Marquardt_algorithm)).



## Additional Relevant Resources

Further readings for the curious and interested, in addition to the research papers mentioned in the codebase itself:

- [A Survey of Indoor Localization Systems and Technologies](https://ieeexplore.ieee.org/document/8692423)
- [A Survey of Smartphone-Based Indoor Positioning System Using RF-Based Wireless Technologies](https://www.mdpi.com/1424-8220/20/24/7230)
- [WiFi-Based Indoor Positioning System](https://ieeexplore.ieee.org/document/5474534)
- [Evolution of Indoor Positioning Technologies: A Survey](https://www.hindawi.com/journals/js/2017/2630413/)
- [Indoor Positioning System Using Bluetooth Low Energy](https://ieeexplore.ieee.org/document/7915011)
- [WiFi Positioning: A Survey](https://www.researchgate.net/publication/237046557_WiFi_Positioning_A_Survey)
- [Evaluation of The Reliability of RSSI for Indoor Localization](https://ieeexplore.ieee.org/document/6402492)
- [A Polygonal Method for Ranging-Based Localization in an Indoor Wireless Sensor Network](https://link.springer.com/article/10.1007/s11277-011-0306-7)
- [An Enhanced K-Nearest-Neighbor Algorithm for Indoor Positioning Systems in a WLAN](https://ieeexplore.ieee.org/document/7017163)
- [Comparison of WiFi-Based Indoor Positioning Techniques](http://publikacio.uni-eszterhazy.hu/11/)
- [A Comparison of WiFi-Based Indoor Positioning Methods](https://ieeexplore.ieee.org/document/9008751)
- [Deep Neural Networks for Indoor Localization Using WiFi Fingerprints](https://link.springer.com/chapter/10.1007/978-3-030-22885-9_21)
- [Using Fuzzy Logic to Improve BLE Indoor Positioning System](https://link.springer.com/chapter/10.1007/978-3-319-31165-4_18)
- [Improving Indoor Localization Using Bluetooth Low Energy Beacons](https://www.hindawi.com/journals/misy/2016/2083094/)
- [Localization Algorithm With On-Line Path Loss Estimation and Node Selection](https://www.mdpi.com/1424-8220/11/7/6905)
- [Localization Algorithm Based on Iterative Centroid Estimation for Wireless Sensor Networks](https://www.hindawi.com/journals/mpe/2018/5456191/)
- [A Location Estimation Algorithm Based on RSSI Vector Similarity Degree](https://journals.sagepub.com/doi/10.1155/2014/371350)
- [WiFi Indoor Localization Using Channel State Information](http://resolver.tudelft.nl/uuid:6d5a3afd-1966-4357-b063-7a82c0fdb0ab)
- [Estimation of the Path-Loss Exponent by Bayesian Filtering Method](https://www.mdpi.com/1424-8220/21/6/1934)
- [Improving Fingerprint Indoor Localization Using Convolutional Neural Networks](https://ieeexplore.ieee.org/document/9237969)
- [UJIIndoorLoc: A New Multi-Building and Multi-Floor Database for WLAN Fingerprint-Based Indoor Localization Problems](https://ieeexplore.ieee.org/document/7275492)

More resources such as proprietary localization service SDKs and code samples/tutorials on open-source GitHub repositories can be found online. Google is your friend.

