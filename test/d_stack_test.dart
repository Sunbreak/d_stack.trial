import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:d_stack/d_stack.dart';

void main() {
  const MethodChannel channel = MethodChannel('d_stack');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await DStack.platformVersion, '42');
  });
}
